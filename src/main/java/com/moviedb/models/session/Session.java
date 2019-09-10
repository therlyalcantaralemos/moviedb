package com.moviedb.models.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moviedb.models.movie.Movie;
import com.moviedb.models.theater.Theater;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

@Data
public class Session {
    @Id
    private String id;
    private String room;
    private String date;
    private String type;
    private Theater theater;
    private Movie movie;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Version
    @JsonIgnore
    private Long version;

    public void updateWith(Session otherSession) {
        this.room = otherSession.getRoom();
        this.date = otherSession.getDate();
        this.type = otherSession.getType();
        this.theater = otherSession.getTheater();
        this.movie = otherSession.getMovie();
    }
}
