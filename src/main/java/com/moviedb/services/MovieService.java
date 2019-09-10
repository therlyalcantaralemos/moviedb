package com.moviedb.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviedb.models.movie.Credit;
import com.moviedb.models.movie.Movie;
import com.moviedb.models.movie.MovieDTO;
import com.moviedb.models.themoviedb.*;
import com.moviedb.repositories.MovieRepository;
import com.moviedb.services.exceptions.ApiResultNotFoundException;
import com.moviedb.services.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MovieService {
    private TheMovieDBService theMovieDBService;
    private MovieRepository movieRepository;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    public MovieService(TheMovieDBService theMovieDBService, MovieRepository movieRepository, ObjectMapper objectMapper) {
        this.theMovieDBService = theMovieDBService;
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
    }

    private List<MovieResult> getAllMoviesApiNowPlaying(){
        MovieTheMovieDB movie = theMovieDBService.getMovieNowPlayingApi(1);
        if(Objects.nonNull(movie.getResults())){
            getMoviesApiNowPlayingPages(movie);
            return movie.getResults();
        }else{
            throw new ApiResultNotFoundException();
        }
    }

    private void getMoviesApiNowPlayingPages(MovieTheMovieDB movieTheMovieDB){
        if(movieTheMovieDB.getTotalPages() > 1) {
            Stream.iterate(2, n -> n + 1)
                    .limit((movieTheMovieDB.getTotalPages() - 1))
                    .forEach(page ->
                        movieTheMovieDB.getResults().addAll(
                                Objects.requireNonNull(theMovieDBService.getMovieNowPlayingApi(page)).getResults()
                        )
                    );
        }
    }

    private Stream<Movie> getMoviesWithCreditAndGenrer(List<Movie> movieResults){
       return movieResults.stream()
               .peek(movie -> {
                   CreditTheMovieDB credit = theMovieDBService.getCreditsAndGenrer(movie.getMovieId());
                   if(Objects.nonNull(credit)){
                       movie.setGenres(getGenre(credit.getGenres()));
                       movie.setCredits(getCredit(credit.getCredits()));
                   }
               });
    }

    private List<String> getGenre(List<GenreResult> genreResult){
        return (Objects.nonNull(genreResult)) ? genreResult.stream().map(GenreResult::getName).collect(Collectors.toList()) : null;
    }

    private Credit getCredit(CreditResult creditResult){
        return new Credit(getDirector(creditResult.getCrew()), getCast(creditResult.getCast()));
    }

    private List<String> getCast(List<Person> cast){
        return (Objects.nonNull(cast)) ? cast.stream().map(Person::getName).collect(Collectors.toList()) : null;
    }

    private List<String> getDirector(List<Person> crew){
        return (Objects.nonNull(crew)) ? crew.stream().filter(job -> job.getJob().equals("Director")).map(Person::getName).collect(Collectors.toList()) : null;
    }

    private List<Movie> getMovieFromMovieResult(){
         return getAllMoviesApiNowPlaying().stream()
                .map(movieDB -> {
                    Movie movieMapper = objectMapper.convertValue(movieDB, Movie.class);
                    movieMapper.setMovieId(movieDB.getId());
                    movieMapper.setId(null);
                    return movieMapper;
                }).collect(Collectors.toList());
    }

    private Stream<Movie> getMovies(){
      return getMoviesWithCreditAndGenrer(getMovieFromMovieResult());
    }

    @Transactional
    public void createOrUpdate(){
        LocalDateTime localTime = LocalDateTime.now();

        getMovies().forEach(movie ->
                movieRepository.findByMovieId(movie.getMovieId())
                    .ifPresentOrElse(movieOpt ->  {
                            movieOpt.updateWith(movie);
                            movieRepository.save(movieOpt);
                            logger.error("updated movie: {}", movieOpt.getMovieId());
                        }, () -> {
                            movieRepository.save(movie);
                            logger.error("created movie: {}", movie.getMovieId());
                        })
            );

        movieRepository.deleteByUpdatedAtLessThan(localTime);
    }

    public Page<MovieDTO> listByGenre(String name, Pageable page){
        Page<Movie> movieGenres = movieRepository.findByGenresIgnoreCaseIn(Collections.singletonList(name), page);
        if(!movieGenres.isEmpty()){
            return movieGenres.map(movie -> objectMapper.convertValue(movie, MovieDTO.class));
        }else{
            throw new ObjectNotFoundException();
        }
    }

    public Page<MovieDTO> listAll(Pageable page){
        Page<Movie> movies = movieRepository.findAll(page);
        if(!movies.isEmpty()){
            return movies.map(movie -> objectMapper.convertValue(movie, MovieDTO.class));
        }else{
            throw new ObjectNotFoundException();
        }
    }
}
