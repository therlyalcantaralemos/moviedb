package com.moviedb.models.themoviedb;

import lombok.Data;

@Data
public class Person {
    private Integer castId;
    private String character;
    private String creditId;
    private Integer id;
    private Integer gender;
    private String name;
    private Integer order;
    private String profilePath;
    private String job;
    private String department;
}
