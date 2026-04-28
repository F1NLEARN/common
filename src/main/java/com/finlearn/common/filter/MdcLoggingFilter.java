package com.finlearn.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;

public class MdcLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // URI 및 Method 정보 추출 (traceId는 Micrometer가 자동으로 MDC에 주입)
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        MDC.put("uri", uri);
        MDC.put("method", method);

        try {
            chain.doFilter(request, response);
        } finally {
            // 요청 종료 시 반드시 클리어
            MDC.clear();
        }

    }
}
