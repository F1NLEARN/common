package com.finlearn.common.response;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String code,
        Object message,
        String field,
        String traceId,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(HttpStatus status, Object message) {
        return new ErrorResponse(
                status.value(),
                status.name(),
                null,
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
                null,
                message,
                field,
                MDC.get("traceId"),
                LocalDateTime.now()
        );
    }

    public static ErrorResponse of(HttpStatus status, String code, String field, Object message) {
        return new ErrorResponse(
                status.value(),
                status.name(),
                code,
                message,
                field,
                MDC.get("traceId"),
                LocalDateTime.now()
        );
    }
}
