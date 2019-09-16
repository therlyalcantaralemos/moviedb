package com.moviedb.models.movie;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionRequestDTO {
    private String idMovie;
    private String room;
    private LocalDateTime date;
    private String type;
}
