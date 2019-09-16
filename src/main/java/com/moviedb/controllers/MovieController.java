package com.moviedb.controllers;

import com.moviedb.models.movie.*;
import com.moviedb.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/{theaterId}/movie")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<MovieDTO> listAll(@PathVariable("theaterId") String theaterId, Pageable page){
        return movieService.listAll(theaterId, page);
    }

    @GetMapping("/genre")
    @ResponseStatus(value = HttpStatus.OK)
    public Page<MovieDTO> listByGenre(@PathVariable("theaterId") String theaterId,
                                      @RequestParam(value="name", required = true) String name,
                                      Pageable page){
        return movieService.listByGenre(theaterId, name, page);
    }

    @PostMapping("/session")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SessionDTO createSessionInMovie(@PathVariable("theaterId") String theaterId,
                                           @RequestBody SessionRequestDTO sessionDTO){
        return movieService.createSessionInMovie(theaterId, sessionDTO);
    }

    @PutMapping("/session/{sessionId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateSessionInMovie(@PathVariable("theaterId") String theaterId,
                                     @PathVariable("sessionId") String sessionId,
                                     @RequestBody SessionUpdateDTO sessionUpdateDTO){
        movieService.updateSessionInMovie(theaterId, sessionId, sessionUpdateDTO);
    }

    @DeleteMapping("/session/{sessionId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("theaterId") String theaterId,
                       @PathVariable("sessionId") String sessionId){
        movieService.deleteSessionInMovie(theaterId, sessionId);
    }

    @GetMapping("/session/{sessionId}")
    @ResponseStatus(value = HttpStatus.OK)
    public SessionDTO listSession(@PathVariable("theaterId") String theaterId,
                                  @PathVariable("sessionId") String sessionId){
        return movieService.listSession(theaterId, sessionId);
    }

}
