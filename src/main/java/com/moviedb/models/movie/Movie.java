package com.moviedb.models.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {
    @Id
    private String id;
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
    private List<String> genres;
    private Credit credits;
    private List<Session> sessions;
    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdAt;
    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updatedAt;
    @Version
    @JsonIgnore
    private Long version;

    public void updateWith(Movie otherMovie){
        this.popularity = otherMovie.getPopularity();
        this.voteCount = otherMovie.getVoteCount();
        this.video = otherMovie.getVideo();
        this.posterPath = otherMovie.getPosterPath();
        this.movieId = otherMovie.getMovieId();
        this.adult = otherMovie.getAdult();
        this.backdropPath = otherMovie.getBackdropPath();
        this.originalLanguage = otherMovie.getOriginalLanguage();
        this.originalTitle = otherMovie.getOriginalTitle();
        this.title = otherMovie.getTitle();
        this.voteAverage = otherMovie.getVoteAverage();
        this.overview = otherMovie.getOverview();
        this.releaseDate = otherMovie.getReleaseDate();
        this.genres = otherMovie.getGenres();
        this.credits = otherMovie.getCredits();
        this.sessions = otherMovie.getSessions();

    }
}
