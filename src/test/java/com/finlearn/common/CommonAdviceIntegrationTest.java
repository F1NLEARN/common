package com.finlearn.common;

import com.finlearn.common.exception.BadRequestException;
import com.finlearn.common.exception.GlobalExceptionAdviceImpl;
import com.finlearn.common.response.CommonResponseAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommonAdviceIntegrationTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(
                        new CommonResponseAdvice(),
                        new GlobalExceptionAdviceImpl()
                )
                .build();
    }

    @Test
    @DisplayName("정상 응답은 CommonResponse로 감싸진다")
    void successResponseWrapped() throws Exception {
        mockMvc.perform(get("/success"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andExpect(jsonPath("$.data.value").value("ok"));
    }

    @Test
    @DisplayName("CustomException은 ErrorResponse로 변환된다")
    void customExceptionHandled() throws Exception {
        mockMvc.perform(get("/fail"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));
    }

    @RestController
    static class TestController {

        @GetMapping("/success")
        public TestDto success() {
            return new TestDto("ok");
        }

        @GetMapping("/fail")
        public TestDto fail() {
            throw new BadRequestException("잘못된 요청입니다.");
        }
    }

    record TestDto(String value) {
    }
}