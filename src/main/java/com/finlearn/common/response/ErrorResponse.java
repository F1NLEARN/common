package com.finlearn.common.response;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        Object message,
        String field,
        String traceId,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(HttpStatus status, Object message) {
        return new ErrorResponse(
                status.value(),
                status.name(),
                message,
                null,
                MDC.get("traceId"),
                LocalDateTime.now()
        );
    }

    public static ErrorResponse of(HttpStatus status, String field, Object message) {
        return new ErrorResponse(
                status.value(),
                status.name(),
                message,
                field,
                MDC.get("traceId"),
                LocalDateTime.now()
        );
    }
}
