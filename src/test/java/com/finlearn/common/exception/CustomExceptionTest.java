package com.finlearn.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class CustomExceptionTest {

    @Test
    @DisplayName("CustomException은 message와 status를 저장한다")
    void customExceptionStoresMessageAndStatus() {
        CustomException exception = new CustomException("테스트 예외", HttpStatus.BAD_REQUEST);

        assertThat(exception.getMessage()).isEqualTo("테스트 예외");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getField()).isNull();
    }

    @Test
    @DisplayName("CustomException은 field를 함께 저장할 수 있다")
    void customExceptionStoresField() {
        CustomException exception = new CustomException("email", "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);

        assertThat(exception.getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getField()).isEqualTo("email");
    }

    @Test
    @DisplayName("status가 null이면 INTERNAL_SERVER_ERROR를 기본값으로 사용한다")
    void customExceptionUsesDefaultStatusWhenNull() {
        CustomException exception = new CustomException("기본 에러", null);

        assertThat(exception.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}