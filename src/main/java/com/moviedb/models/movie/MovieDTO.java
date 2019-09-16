package com.moviedb.models.movie;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moviedb.models.themoviedb.CreditResult;
import com.moviedb.models.themoviedb.GenreResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieDTO implements Serializable {
    private Double popularity;
    private Integer voteCount;
    private Boolean video;
    private String posterPath;
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
    private List<Session> sessions;

}
