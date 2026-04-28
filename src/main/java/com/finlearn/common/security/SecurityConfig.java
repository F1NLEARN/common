package com.finlearn.common.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 다운스트림 마이크로서비스용 기본 Security 설정.
 * 서비스가 직접 SecurityFilterChain 빈을 등록하면 이 설정은 적용되지 않는다.
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

    @Bean
    @ConditionalOnMissingBean
    public UserHeaderFilter userHeaderFilter() {
        return new UserHeaderFilter();
    }

    /**
     * Gateway에서 이미 JWT 검증을 완료했으므로 모든 요청을 허용하고,
     * UserHeaderFilter로 SecurityContext만 구성한다.
     * 서비스별 SecurityFilterChain이 있으면 이 빈은 생성되지 않는다.
     */
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http,
                                                   UserHeaderFilter userHeaderFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(userHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
