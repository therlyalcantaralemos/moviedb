package com.moviedb.repositories;

import com.moviedb.models.theater.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheaterRepository extends MongoRepository<Theater, String> {

    Optional<Theater> findByName(String name);

}
