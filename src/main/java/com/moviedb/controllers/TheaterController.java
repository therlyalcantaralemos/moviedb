package com.moviedb.controllers;

import com.moviedb.models.theater.Theater;
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
    public List<Theater> listAll(){
        return theaterService.listAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Theater listById(@PathVariable String id){
        return theaterService.listById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Theater create(@Valid @RequestBody TheaterDTO theaterDTO){
        return theaterService.create(theaterDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") String id, @Valid @RequestBody TheaterDTO theaterDTO){
        theaterService.update(id, theaterDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        theaterService.delete(id);
    }


}
