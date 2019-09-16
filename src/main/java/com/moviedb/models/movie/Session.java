package com.moviedb.models.movie;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Session {
    private String id;
    private String theaterId;
    private String room;
    private LocalDateTime date;
    private String type;
}
