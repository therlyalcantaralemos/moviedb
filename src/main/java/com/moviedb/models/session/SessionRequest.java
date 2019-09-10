package com.moviedb.models.session;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class SessionRequest {
    @NotEmpty
    private String room;
    @NotEmpty
    private LocalDateTime date;
    @NotEmpty
    private String type;
    @NotEmpty
    private String movieId;
    @NotEmpty
    private String theaterId;
}
