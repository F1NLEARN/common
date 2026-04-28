package com.finlearn.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/** Gateway가 검증 후 주입한 X-User-* 헤더를 읽어 SecurityContext를 구성하는 필터 */
public class UserHeaderFilter extends OncePerRequestFilter {

    public static final String HEADER_USER_ID    = "X-User-Id";
    public static final String HEADER_USER_EMAIL = "X-User-Email";
    public static final String HEADER_USER_ROLE  = "X-User-Role";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String userIdHeader = request.getHeader(HEADER_USER_ID);
        String email        = request.getHeader(HEADER_USER_EMAIL);
        String role         = request.getHeader(HEADER_USER_ROLE);

        // userId와 role이 있으면 SecurityContext 구성 (email은 JWT에 포함되지 않아 null일 수 있음)
        if (userIdHeader != null && role != null) {
            UUID userId = UUID.fromString(userIdHeader);
            HeaderUserDetails userDetails = new HeaderUserDetails(userId, email, role);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
            );
        }

        filterChain.doFilter(request, response);
    }
}
