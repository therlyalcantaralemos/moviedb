package com.moviedb.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviedb.models.movie.Movie;
import com.moviedb.models.session.Session;
import com.moviedb.models.session.SessionDTO;
import com.moviedb.models.theater.Theater;
import com.moviedb.repositories.MovieRepository;
import com.moviedb.repositories.SessionRepository;
import com.moviedb.repositories.TheaterRepository;
import com.moviedb.services.exceptions.DocumentExistsException;
import com.moviedb.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    private SessionRepository sessionRepository;
    private TheaterRepository theaterRepository;
    private MovieRepository movieRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public SessionService(SessionRepository sessionRepository,
                          TheaterRepository theaterRepository,
                          MovieRepository movieRepository,
                          ObjectMapper objectMapper) {
        this.sessionRepository = sessionRepository;
        this.theaterRepository = theaterRepository;
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
    }

    public Session create(SessionDTO sessionDTO) {
        Theater theater = theaterRepository.findById(sessionDTO.getTheaterId()).orElseThrow(ObjectNotFoundException::new);
        Movie movie = movieRepository.findByMovieId(Long.parseLong(sessionDTO.getMovieId())).orElseThrow(ObjectNotFoundException::new);

        sessionRepository.findByTheaterAndMovieAndRoomAndDateBetween(theater, movie, sessionDTO.getRoom(), sessionDTO.getDate().withMinute(0).withSecond(0), sessionDTO.getDate().withMinute(59).withSecond(59)).ifPresent(session -> {throw new DocumentExistsException();});

        Session session = objectMapper.convertValue(sessionDTO, Session.class);
        session.setMovie(movie);
        session.setTheater(theater);

        return sessionRepository.save(session);
    }

    public void update(String id, SessionDTO sessionDTO){
        sessionRepository.findById(id).ifPresentOrElse(session -> {
            session.updateWith(objectMapper.convertValue(sessionDTO, Session.class));
            sessionRepository.save(session);
        }, () -> {throw new ObjectNotFoundException(); });
    }

    public void delete(String id) {
        sessionRepository.findById(id).ifPresentOrElse(session -> sessionRepository.deleteById(id), () -> {throw new ObjectNotFoundException(); });
    }

    public SessionDTO listById(String id) {
        return objectMapper.convertValue(sessionRepository.findById(id).orElseThrow(ObjectNotFoundException::new), SessionDTO.class);
    }

    public List<Session> listByIdTheater(String idTheater) {
        return sessionRepository.findByTheater(theaterRepository.findById(idTheater).orElseThrow(ObjectNotFoundException::new));
    }
}
