package com.moviedb.Factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviedb.models.movie.*;
import com.moviedb.models.theater.Theater;
import com.moviedb.models.themoviedb.MovieResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MovieFactory {

    private final ObjectMapper objectMapper;

    @Autowired
    public MovieFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Movie getMovieResultMovieConverter(MovieResult movieResult){
        Movie movieMapper = objectMapper.convertValue(movieResult, Movie.class);
        movieMapper.setMovieId(movieResult.getId());
        movieMapper.setId(null);
        return movieMapper;
    }

    public MovieDTO getMovieMovieDtoConverter(Movie movie){
        return objectMapper.convertValue(movie, MovieDTO.class);
    }

    public Session getSessionFromSessionRequestDTO(SessionRequestDTO sessionRequestDto, String  theaterId){
        Session session = objectMapper.convertValue(sessionRequestDto, Session.class);
        session.setId(UUID.randomUUID().toString());
        session.setTheaterId(theaterId);
        return session;
    }

    public MovieSessionDTO getSessionDtoFromSession(Session session, Theater theater, Movie movie){
        MovieSessionDTO sessionDto = objectMapper.convertValue(session, MovieSessionDTO.class);
        sessionDto.setMovie(movie);
        sessionDto.setTheater(theater);
        return sessionDto;
    }

}
