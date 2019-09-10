package com.moviedb.repositories;

import com.moviedb.models.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    Optional<Movie> findByMovieId(Long movieId);

    void deleteByUpdatedAtLessThan(LocalDateTime updatedAt);

    Page<Movie> findByGenresIgnoreCaseIn(String name, Pageable pageable);

}
