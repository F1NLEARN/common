package com.finlearn.common.config;

import com.finlearn.common.security.UserAuditorAware;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.url")
@EnableJpaAuditing
public class JpaAuditConfig {

    @Bean
    @ConditionalOnMissingBean
    public AuditorAware<UUID> auditorProvider() {
        return new UserAuditorAware();
    }
}
