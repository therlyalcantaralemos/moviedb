package com.moviedb.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ApiResultNotFoundException extends RuntimeException {
    public ApiResultNotFoundException(String msg) {
        super(msg);
    }

    public ApiResultNotFoundException() {
    }
}
