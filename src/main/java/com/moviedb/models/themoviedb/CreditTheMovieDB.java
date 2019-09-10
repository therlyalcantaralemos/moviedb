package com.moviedb.models.themoviedb;

import lombok.Data;

import java.util.List;

@Data
public class CreditTheMovieDB {
    private List<GenreResult> genres;
    private CreditResult credits;
}
