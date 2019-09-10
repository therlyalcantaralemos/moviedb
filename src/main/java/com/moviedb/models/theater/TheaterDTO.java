package com.moviedb.models.theater;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TheaterDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String address;
}
