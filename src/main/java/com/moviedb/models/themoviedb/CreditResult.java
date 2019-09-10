package com.moviedb.models.themoviedb;

import lombok.Data;

import java.util.List;

@Data
public class CreditResult {
    private List<Person> cast;
    private List<Person> crew;
}
