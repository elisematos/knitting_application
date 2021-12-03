package com.application.knitting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatternNotFoundException extends RuntimeException {
    public PatternNotFoundException(String message) {
    }
}
