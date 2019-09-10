package com.moviedb.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviedb.models.theater.Theater;
import com.moviedb.models.theater.TheaterDTO;
import com.moviedb.repositories.MovieRepository;
import com.moviedb.repositories.TheaterRepository;
import com.moviedb.services.exceptions.DocumentExistsException;
import com.moviedb.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {
    private TheaterRepository theaterRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository, ObjectMapper objectMapper) {
        this.theaterRepository = theaterRepository;
        this.objectMapper = objectMapper;
    }

    public TheaterDTO create(TheaterDTO theaterDTO){
        theaterRepository.findByName(theaterDTO.getName()).ifPresent(findTheater -> {
            throw new DocumentExistsException();
        });

        Theater theater = theaterRepository.save(objectMapper.convertValue(theaterDTO, Theater.class));
        theaterDTO.setId(theater.getId());
        return theaterDTO;
    }

    public void update(TheaterDTO theaterDTO){
        Theater theater = theaterRepository.findById(theaterDTO.getId()).orElseThrow(ObjectNotFoundException::new);
        theater.updateWith(objectMapper.convertValue(theaterDTO, Theater.class));
        theaterRepository.save(theater);
    }

    public void delete(String id) {
        theaterRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
        theaterRepository.deleteById(id);
    }

    public List<TheaterDTO> listAll(){
        List<Theater> theaters = theaterRepository.findAll();
        if(!theaters.isEmpty()){
            return theaters.stream().map(theater -> objectMapper.convertValue(theater, TheaterDTO.class)).collect(Collectors.toList());
        }else{
            throw new ObjectNotFoundException();
        }
    }

    public TheaterDTO listById(String id) {
        Theater theater = theaterRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
        return objectMapper.convertValue(theater, TheaterDTO.class);
    }

}
