package com.moviedb.models.movie;

import com.moviedb.models.theater.Theater;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieSessionDTO {
    private String id;
    private String room;
    private LocalDateTime date;
    private String type;
    private Movie movie;
    private Theater theater;
}
