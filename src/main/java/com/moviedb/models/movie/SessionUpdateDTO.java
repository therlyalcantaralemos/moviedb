package com.moviedb.models.movie;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionUpdateDTO {
    private String room;
    private LocalDateTime date;
    private String type;
}
