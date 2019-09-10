package com.moviedb.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviedb.models.theater.Theater;
import com.moviedb.models.theater.TheaterDTO;
import com.moviedb.repositories.TheaterRepository;
import com.moviedb.services.exceptions.DocumentExistsException;
import com.moviedb.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {
    private TheaterRepository theaterRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository, ObjectMapper objectMapper) {
        this.theaterRepository = theaterRepository;
        this.objectMapper = objectMapper;
    }

    public Theater create(TheaterDTO theaterDTO){
        theaterRepository.findByName(theaterDTO.getName()).ifPresent(findTheater -> { throw new DocumentExistsException(); });
        return theaterRepository.save(objectMapper.convertValue(theaterDTO, Theater.class));
    }

    public void update(String id, TheaterDTO theaterDTO){
        Theater theater = theaterRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
        theater.updateWith(objectMapper.convertValue(theaterDTO, Theater.class));
        theaterRepository.save(theater);
    }

    public void delete(String id) {
        theaterRepository.delete(theaterRepository.findById(id).orElseThrow(ObjectNotFoundException::new));
    }

    public List<Theater> listAll(){
        List<Theater> theaters = theaterRepository.findAll();
        if(!theaters.isEmpty()){
            return theaters;
        }else{
            throw new ObjectNotFoundException();
        }
    }

    public Theater listById(String id) {
        return theaterRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
    }

}
