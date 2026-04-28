package com.finlearn.common.util;

import com.finlearn.common.security.HeaderUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/** SecurityContext에서 현재 인증된 유저 정보를 꺼내는 편의 유틸리티. */
public class SecurityUtil {

    private SecurityUtil() {}

    // 현재 요청의 userId를 반환한다.
    public static UUID getCurrentUserId() {
        return getUserDetails().getUserId();
    }

    // 현재 요청의 email을 반환한다.
    public static String getCurrentUserEmail() {
        return getUserDetails().getEmail();
    }

    // 현재 요청의 role을 반환한다.
    public static String getCurrentUserRole() {
        return getUserDetails().getRole();
    }

    private static HeaderUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof HeaderUserDetails)) {
            throw new IllegalStateException("인증된 사용자 정보가 없다.");
        }
        return (HeaderUserDetails) authentication.getPrincipal();
    }
}
