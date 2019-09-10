package com.moviedb.repositories;

import com.moviedb.models.movie.Movie;
import com.moviedb.models.session.Session;
import com.moviedb.models.theater.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

    Optional<Session> findByTheaterAndMovie(Theater theater, Movie movie);

    Optional<Session> findByTheaterAndMovieAndRoomAndDateBetween(Theater theater, Movie movie, String room, LocalDateTime from, LocalDateTime to);

    List<Session> findByTheater(Theater theater);
}

