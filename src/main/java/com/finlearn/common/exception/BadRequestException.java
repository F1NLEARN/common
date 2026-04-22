package com.finlearn.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String field, String message) {
        super(field, message, HttpStatus.BAD_REQUEST);
    }
}
