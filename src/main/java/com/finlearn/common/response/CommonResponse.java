package com.finlearn.common.response;

import org.slf4j.MDC;

public record CommonResponse<T>(
        boolean success,
        String message,
        T data,
        String traceId
) {
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(
                true,
                "요청이 성공적으로 처리되었습니다.",
                data,
                MDC.get("traceId")
        );
    }

    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>(
                true,
                message,
                data,
                MDC.get("traceId")
        );
    }
}
