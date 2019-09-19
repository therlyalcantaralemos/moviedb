package com.moviedb.services;

import com.moviedb.Factories.MovieFactory;
import com.moviedb.models.movie.*;
import com.moviedb.models.theater.Theater;
import com.moviedb.models.themoviedb.*;
import com.moviedb.repositories.MovieRepository;
import com.moviedb.repositories.TheaterRepository;
import com.moviedb.services.exceptions.ApiResultNotFoundException;
import com.moviedb.services.exceptions.DocumentExistsException;
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
    private TheaterRepository theaterRepository;
    private final MovieFactory movieFactory;

    private final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    public MovieService(TheMovieDBService theMovieDBService,
                        MovieRepository movieRepository,
                        TheaterRepository theaterRepository,
                        MovieFactory movieFactory) {
        this.theMovieDBService = theMovieDBService;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.movieFactory = movieFactory;
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
                    .forEach(page -> movieTheMovieDB.getResults().addAll( Objects.requireNonNull(theMovieDBService.getMovieNowPlayingApi(page)).getResults()));
        }
    }

    private Stream<Movie> getMoviesWithCreditAndGenrer(List<Movie> movieResults){
       return movieResults.stream().map(this::getCreditsAndGenre);
    }

    private Movie getCreditsAndGenre(Movie movie){
        CreditTheMovieDB credit = theMovieDBService.getCreditsAndGenrer(movie.getMovieId());
        if(Objects.nonNull(credit)){
            movie.setGenres(getGenre(credit.getGenres()));
            movie.setCredits(getCredits(credit.getCredits()));
        }
        return movie;
    }

    private List<String> getGenre(List<GenreResult> genreResult){
        return (Objects.nonNull(genreResult)) ? genreResult.stream().map(GenreResult::getName).collect(Collectors.toList()) : null;
    }

    private Credit getCredits(CreditResult creditResult){
        List<String> director = (Objects.nonNull(creditResult.getCrew())) ? creditResult.getCrew().stream().filter(job -> job.getJob().equals("Director")).map(Person::getName).collect(Collectors.toList()) : null;
        List<String> cast = (Objects.nonNull(creditResult.getCast())) ? creditResult.getCast().stream().map(Person::getName).collect(Collectors.toList()) : null;
        return new Credit(director, cast);
    }

    private List<Movie> getMovieFromMovieResult(){
         return getAllMoviesApiNowPlaying().stream().map(movieFactory::getMovieResultMovieConverter).collect(Collectors.toList());
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

    private Page<MovieDTO> verifyMovie(Page<Movie> movie){
        if(!movie.isEmpty()){
            return movie.map(movieFactory::getMovieMovieDtoConverter);
        }else{
            throw new ObjectNotFoundException();
        }
    }

    public Page<MovieDTO> listByGenre(String theaterId, String name, Pageable page){
        return verifyMovie(movieRepository.findBySessions_TheaterIdAndGenresIgnoreCaseIn(theaterId, Collections.singletonList(name), page));
    }

    public Page<MovieDTO> listAll(String theaterId, Pageable page){
        return verifyMovie(movieRepository.findBySessions_TheaterId(theaterId, page));
    }

    public MovieSessionDTO createSessionInMovie(String theaterId, SessionRequestDTO sessionRequestDto) {
        Movie movie = movieRepository.findByMovieId(Long.parseLong(sessionRequestDto.getIdMovie())).orElseThrow(ObjectNotFoundException::new);
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(ObjectNotFoundException::new);
        movieRepository.findSessionsByTheaterAndRoomAndDateQuery(theaterId, sessionRequestDto.getRoom(), sessionRequestDto.getDate()).ifPresent( findSession -> { throw new DocumentExistsException(); });

        Session session = movieFactory.getSessionFromSessionRequestDTO(sessionRequestDto, theaterId);

        if(Objects.isNull(movie.getSessions())){
            movie.setSessions(Collections.singletonList(session));
        }else{
            movie.getSessions().add(session);
        }

        movieRepository.save(movie);
        return movieFactory.getSessionDtoFromSession(session, theater, movie);
    }

    public void updateSessionInMovie(String theaterId, String sessionId, SessionUpdateDTO sessionUpdateDTO) {
        Movie movie = movieRepository.findBySessions_TheaterIdAndSessions_Id(theaterId, sessionId).orElseThrow(ObjectNotFoundException::new);
        movieRepository.findSessionsByIdAndTheaterAndRoomAndDateQuery(sessionId, theaterId, sessionUpdateDTO.getRoom(), sessionUpdateDTO.getDate()).ifPresent( findSession -> { throw new DocumentExistsException(); });
        movie.getSessions().forEach(session -> {
                    if(session.getId().equals(sessionId)){
                        session.setDate(sessionUpdateDTO.getDate());
                        session.setRoom(sessionUpdateDTO.getRoom());
                        session.setType(sessionUpdateDTO.getType());
                    }
                });

        movieRepository.save(movie);
    }

    public void deleteSessionInMovie(String theaterId, String sessionId){
        Movie movie = movieRepository.findBySessions_TheaterIdAndSessions_Id(theaterId, sessionId).orElseThrow(ObjectNotFoundException::new);
        movie.getSessions().removeIf(session -> session.getId().equals(sessionId));
        movieRepository.save(movie);
    }

    public MovieSessionDTO listSession(String theaterId, String sessionId) {
        Movie movie = movieRepository.findBySessions_TheaterIdAndSessions_Id(theaterId, sessionId).orElseThrow(ObjectNotFoundException::new);
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(ObjectNotFoundException::new);
        return movieFactory.getSessionDtoFromSession(movie.getSessions().stream().filter(findSession -> findSession.getId().equals(sessionId)).findFirst().get(), theater, movie);
    }
}
