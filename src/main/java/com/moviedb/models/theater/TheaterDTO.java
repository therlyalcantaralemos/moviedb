package com.moviedb.models.theater;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TheaterDTO {
    private String Id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String address;
}
