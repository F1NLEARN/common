package com.finlearn.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    @DisplayName("ErrorResponse.of(status, message)는 field 없이 생성된다")
    void createErrorResponseWithoutField() {
        MDC.put("traceId", "trace-error-1");

        ErrorResponse response = ErrorResponse.of(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다.");

        assertThat(response.status()).isEqualTo(404);
        assertThat(response.error()).isEqualTo("NOT_FOUND");
        assertThat(response.message()).isEqualTo("대상을 찾을 수 없습니다.");
        assertThat(response.field()).isNull();
        assertThat(response.traceId()).isEqualTo("trace-error-1");
        assertThat(response.timestamp()).isNotNull();

        MDC.clear();
    }

    @Test
    @DisplayName("ErrorResponse.of(status, field, message)는 field를 포함한다")
    void createErrorResponseWithField() {
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, "email", "이메일 형식이 올바르지 않습니다.");

        assertThat(response.status()).isEqualTo(400);
        assertThat(response.error()).isEqualTo("BAD_REQUEST");
        assertThat(response.field()).isEqualTo("email");
        assertThat(response.message()).isEqualTo("이메일 형식이 올바르지 않습니다.");
    }
}