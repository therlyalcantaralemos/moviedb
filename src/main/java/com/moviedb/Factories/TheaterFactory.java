package com.moviedb.Factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviedb.models.theater.Theater;
import com.moviedb.models.theater.TheaterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TheaterFactory {
    private final ObjectMapper objectMapper;

    @Autowired
    public TheaterFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Theater getTheaterFromTheaterDto(TheaterDTO theaterDto){
        return objectMapper.convertValue(theaterDto, Theater.class);
    }


}
