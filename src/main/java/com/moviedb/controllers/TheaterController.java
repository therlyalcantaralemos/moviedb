package com.moviedb.controllers;

import com.moviedb.models.theater.TheaterDTO;
import com.moviedb.services.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/theater")
public class TheaterController {
    private TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    private List<TheaterDTO> listAll(){
        return theaterService.listAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    private TheaterDTO listById(@PathVariable String id){
        return theaterService.listById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    private TheaterDTO create(@Valid @RequestBody TheaterDTO theaterRequest){
        return theaterService.create(theaterRequest);
    }

    @PutMapping()
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    private void update(@Valid @RequestBody TheaterDTO theaterRequest){
        theaterService.update(theaterRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    private void delete(@PathVariable String id){
        theaterService.delete(id);
    }


}
