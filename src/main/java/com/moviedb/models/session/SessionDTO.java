package com.moviedb.models.session;

import com.moviedb.models.movie.Movie;
import com.moviedb.models.theater.Theater;
import lombok.Data;

@Data
public class SessionDTO {
    private String room;
    private String date;
    private String type;
    private Theater theater;
    private Movie movie;
}
