package com.moviedb.repositories;

import com.moviedb.models.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    Optional<Movie> findByMovieId(Long movieId);

    void deleteByUpdatedAtLessThan(LocalDateTime updatedAt);

    Page<Movie> findBySessions_TheaterIdAndGenresIgnoreCaseIn(String theaterId, List<String> name, Pageable pageable);

    @Query("{'sessions.theaterId': ?0, 'sessions.room': ?1, 'sessions.date': ?2}")
    Optional<Movie> findSessionsByTheaterAndRoomAndDateQuery(String theaterId, String room, LocalDateTime date);

    Page<Movie> findBySessions_TheaterId(String theaterId, Pageable pageable);

    Optional<Movie> findBySessions_TheaterIdAndSessions_Id(String theaterId, String sessionId);


    @Query("{'sessions.theaterId': {$ne: ?0}, 'sessions.theaterId': ?1, 'sessions.room': ?2, 'sessions.date': ?3}")
    Optional<Movie> findSessionsByIdAndTheaterAndRoomAndDateQuery(String id, String theaterId, String room, LocalDateTime date);

}
