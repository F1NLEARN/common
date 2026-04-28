package com.finlearn.common.config;

import com.finlearn.common.exception.GlobalExceptionAdviceImpl;
import com.finlearn.common.response.CommonResponseAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import com.finlearn.common.filter.MdcLoggingFilter;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CommonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JpaAuditConfig jpaAuditConfig() {
        return new JpaAuditConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisConfig redisConfig() {
        return new RedisConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaConfig kafkaConfig(KafkaProperties kafkaProperties) {
        return new KafkaConfig(kafkaProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionAdviceImpl globalExceptionAdvice() {
        return new GlobalExceptionAdviceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommonResponseAdvice commonResponseAdvice() {
        return new CommonResponseAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<MdcLoggingFilter> mdcLoggingFilter() {
        FilterRegistrationBean<MdcLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MdcLoggingFilter());
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}