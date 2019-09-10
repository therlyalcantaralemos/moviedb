package com.moviedb.models.themoviedb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MovieResult {
    private Double popularity;
    private Integer voteCount;
    private Boolean video;
    private String posterPath;
    @JsonProperty("id")
    private Long movieId;
    private Boolean adult;
    private String backdropPath;
    private String originalLanguage;
    private String originalTitle;
    private String title;
    private Integer voteAverage;
    private String overview;
    private Date releaseDate;
    private List<GenreResult> genres;
    private CreditResult credits;

}
