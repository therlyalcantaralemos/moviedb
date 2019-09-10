package com.moviedb.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviedb.models.movie.Movie;
import com.moviedb.models.session.Session;
import com.moviedb.models.session.SessionDTO;
import com.moviedb.models.session.SessionRequest;
import com.moviedb.models.theater.Theater;
import com.moviedb.repositories.MovieRepository;
import com.moviedb.repositories.SessionRepository;
import com.moviedb.repositories.TheaterRepository;
import com.moviedb.services.exceptions.DocumentExistsException;
import com.moviedb.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public SessionDTO create(SessionRequest sessionRequest) {
        Theater theater = theaterRepository.findById(sessionRequest.getTheaterId()).orElseThrow(ObjectNotFoundException::new);
        Movie movie = movieRepository.findByMovieId(Long.parseLong(sessionRequest.getMovieId())).orElseThrow(ObjectNotFoundException::new);

        sessionRepository.findByTheaterAndMovieAndRoomAndDateBetween(theater, movie, sessionRequest.getRoom(), sessionRequest.getDate().withMinute(0).withSecond(0), sessionRequest.getDate().withMinute(59).withSecond(59)).ifPresent(session -> {throw new DocumentExistsException();});

        Session session = objectMapper.convertValue(sessionRequest, Session.class);
        session.setMovie(movie);
        session.setTheater(theater);

        return objectMapper.convertValue(sessionRepository.save(session), SessionDTO.class);
    }

    public void update(String id, SessionRequest sessionRequest){
        sessionRepository.findById(id).ifPresentOrElse(session -> {
            session.updateWith(objectMapper.convertValue(sessionRequest, Session.class));
            sessionRepository.save(session);
        }, () -> {throw new ObjectNotFoundException(); });
    }

    public void delete(String id) {
        sessionRepository.findById(id).ifPresentOrElse(session -> {
            sessionRepository.deleteById(id);
            }, () -> {throw new ObjectNotFoundException(); });
    }

    public SessionDTO listById(String id) {
        return objectMapper.convertValue(sessionRepository.findById(id).orElseThrow(ObjectNotFoundException::new), SessionDTO.class);
    }

    public List<SessionDTO> listByIdTheater(String idTheater) {
        Theater theater = theaterRepository.findById(idTheater).orElseThrow(ObjectNotFoundException::new);
        return sessionRepository.findByTheater(theater).stream()
                .map(session -> objectMapper.convertValue(session, SessionDTO.class))
                .collect(Collectors.toList());
    }
}
