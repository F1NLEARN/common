package com.finlearn.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OpenApiConfig {

    private static final String BEARER_AUTH = "BearerAuth";
    private static final String USER_ID_HEADER = "XUserId";
    private static final String USER_ROLE_HEADER = "XUserRole";
    private static final String USER_NICKNAME_HEADER = "XUserNickname";

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI finlearnOpenAPI(
            @Value("${spring.application.name:finlearn-service}") String applicationName
    ) {
        return new OpenAPI()
                .info(new Info()
                        .title(toTitle(applicationName))
                        .version("v1")
                        .description("FinLearn API documentation"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, bearerAuthScheme())
                        .addSecuritySchemes(USER_ID_HEADER, apiKeyHeaderScheme("X-User-Id"))
                        .addSecuritySchemes(USER_ROLE_HEADER, apiKeyHeaderScheme("X-User-Role"))
                        .addSecuritySchemes(USER_NICKNAME_HEADER, apiKeyHeaderScheme("X-User-Nickname")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

    private SecurityScheme bearerAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    private SecurityScheme apiKeyHeaderScheme(String headerName) {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(headerName);
    }

    private String toTitle(String applicationName) {
        String title = applicationName.replace('-', ' ').replace('_', ' ');
        return "FinLearn " + title;
    }
}
