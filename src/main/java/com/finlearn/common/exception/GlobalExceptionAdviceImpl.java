package com.finlearn.common.exception;

import com.finlearn.common.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "com.finlearn")
public class GlobalExceptionAdviceImpl {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("[TraceID: {}] Custom Exception: {}", MDC.get("traceId"), e.getMessage(), e);

        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus(), e.getCode(), e.getField(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[TraceID: {}] Validation Exception: {} errors found",
                MDC.get("traceId"),
                e.getBindingResult().getErrorCount(),
                e);

        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : "Invalid value",
                        (existing, replacement) -> existing + ", " + replacement
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("[TraceID: {}] Constraint Violation: {}", MDC.get("traceId"), e.getMessage(), e);

        Map<String, String> errors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> {
                            String path = violation.getPropertyPath().toString();
                            return path.substring(path.lastIndexOf('.') + 1);
                        },
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing + ", " + replacement
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("[TraceID: {}] Message Not Readable: {}", MDC.get("traceId"), e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "요청 본문(Body)이 누락되었거나 형식이 잘못되었습니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("[TraceID: {}] Illegal Argument: {}", MDC.get("traceId"), e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "잘못된 요청 값: " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.error("[TraceID: {}] Unhandled Exception occurred", MDC.get("traceId"), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."));
    }
}
