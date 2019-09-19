package com.moviedb.services;

import com.moviedb.Factories.TheaterFactory;
import com.moviedb.common.Constants;
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
    private TheaterFactory theaterFactory;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository, TheaterFactory theaterFactory) {
        this.theaterRepository = theaterRepository;
        this.theaterFactory = theaterFactory;
    }

    public Theater create(TheaterDTO theaterDto){
        theaterRepository.findByName(theaterDto.getName()).ifPresent(findTheater -> { throw new DocumentExistsException(Constants.THEATER_NAME_EXISTIS); });
        return theaterRepository.save(theaterFactory.getTheaterFromTheaterDto(theaterDto));
    }

    public void update(String id, TheaterDTO theaterDto){
        Theater theater = theaterRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
        theater.updateWith(theaterFactory.getTheaterFromTheaterDto(theaterDto));
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
