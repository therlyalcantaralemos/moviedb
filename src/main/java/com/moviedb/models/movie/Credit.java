package com.moviedb.models.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    private List<String> directors;
    private List<String> cast;
}
