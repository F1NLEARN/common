package com.finlearn.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final String field;
    private final String code;

    public CustomException(String message, HttpStatus status) {
        this(null, null, message, status);
    }

    public CustomException(String field, String message, HttpStatus status) {
        this(null, field, message, status);
    }

    public CustomException(String code, String field, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.field = field;
        this.status = status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}