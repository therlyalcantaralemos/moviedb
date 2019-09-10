package com.moviedb.controllers;

import com.moviedb.models.movie.MovieDTO;
import com.moviedb.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/movie")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<MovieDTO> listAll(Pageable page){
        return movieService.listAll(page);
    }

    @GetMapping("/genre")
    @ResponseStatus(value = HttpStatus.OK)
    public Page<MovieDTO> listByGenre(@RequestParam(value="name", required = true) String name, Pageable page){
        return movieService.listByGenre(name, page);
    }

    @GetMapping("/create")
    @ResponseStatus(value = HttpStatus.OK)
    public void teste(){
        movieService.createOrUpdate();
    }

}
