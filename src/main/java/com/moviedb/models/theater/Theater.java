package com.moviedb.models.theater;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Theater {
    @Id
    private String id;
    private String name;
    private String address;
    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdAt;
    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updatedAt;
    @Version
    @JsonIgnore
    private Long version;

    public void updateWith(Theater otherTheater){
        this.name = otherTheater.getName();
        this.address = otherTheater.getAddress();
    }
}
