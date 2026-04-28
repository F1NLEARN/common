package com.finlearn.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/** JWT 인증/인가 관련 에러 코드 **/
@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {

    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않는다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Access Token이다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token이다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않거나 만료된 Refresh Token이다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없다.");

    private final HttpStatus status;
    private final String message;
}
