package com.moviedb.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DocumentExistsException extends RuntimeException {
    public DocumentExistsException(String msg) {
        super(msg);
    }

    public DocumentExistsException() {
    }
}
