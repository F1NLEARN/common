package com.finlearn.common.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static org.assertj.core.api.Assertions.assertThat;

class CommonResponseTest {

    @Test
    @DisplayName("success(data)는 성공 응답을 생성한다")
    void successWithData() {
        MDC.put("traceId", "trace-123");

        CommonResponse<String> response = CommonResponse.success("hello");

        assertThat(response.success()).isTrue();
        assertThat(response.message()).isEqualTo("요청이 성공적으로 처리되었습니다.");
        assertThat(response.data()).isEqualTo("hello");
        assertThat(response.traceId()).isEqualTo("trace-123");

        MDC.clear();
    }

    @Test
    @DisplayName("success(message, data)는 사용자 정의 메시지를 포함한다")
    void successWithCustomMessage() {
        MDC.put("traceId", "trace-456");

        CommonResponse<String> response = CommonResponse.success("조회 성공", "data");

        assertThat(response.success()).isTrue();
        assertThat(response.message()).isEqualTo("조회 성공");
        assertThat(response.data()).isEqualTo("data");
        assertThat(response.traceId()).isEqualTo("trace-456");

        MDC.clear();
    }
}