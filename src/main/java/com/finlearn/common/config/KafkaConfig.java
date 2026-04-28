package com.finlearn.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Kafka 설정을 관리하는 공통 설정 클래스입니다.
 * 
 * @EnableKafka: Kafka Listener 관련 애노테이션(@KafkaListener 등)을 활성화합니다.
 */
@EnableKafka
@Configuration
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    // Spring Boot가 제공하는 KafkaProperties를 주입받아 application.yml 설정을 활용합니다.
    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Kafka Producer를 생성하는 Factory를 설정합니다.
     * 
     * @ConditionalOnMissingBean: 개별 마이크로서비스에서 ProducerFactory 빈을 정의하지 않았을 때만 이 기본 설정을 사용합니다.
     */
    @Bean
    @ConditionalOnMissingBean
    public ProducerFactory<String, Object> producerFactory() {
        // application.yml의 spring.kafka.producer.* 설정들을 맵으로 가져옵니다.
        Map<String, Object> props = kafkaProperties.buildProducerProperties(null);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Java 8 Date/Time API를 지원하기 위해 커스텀 JsonSerializer를 사용합니다.
        JsonSerializer<Object> jsonSerializer = new JsonSerializer<>(objectMapper());
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), jsonSerializer);
    }

    /**
     * Kafka로 메시지를 전송할 때 사용하는 템플릿 빈을 생성합니다.
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Kafka Consumer를 생성하는 Factory를 설정합니다.
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsumerFactory<String, Object> consumerFactory() {
        // application.yml의 spring.kafka.consumer.* 설정들을 맵으로 가져옵니다.
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // 역직렬화 시 신뢰할 수 있는 패키지 범위를 지정합니다. (기본값: *)
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        // Java 8 Date/Time API를 지원하기 위해 커스텀 JsonDeserializer를 사용합니다.
        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>(Object.class, objectMapper());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }

    /**
     * Kafka 리스너(@KafkaListener)가 사용할 컨테이너 팩토리를 설정합니다.
     */
    @Bean
    @ConditionalOnMissingBean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            CommonErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // 메시지 소비 중 에러 발생 시 처리할 핸들러를 등록합니다.
        factory.setCommonErrorHandler(errorHandler);
        // 리스너의 파라미터로 객체를 바로 받을 수 있게 해주는 컨버터를 등록합니다.
        factory.setRecordMessageConverter(messageConverter());
        return factory;
    }

    /**
     * Kafka 소비 중 발생하는 예외를 처리하는 공통 에러 핸들러입니다.
     */
    @Bean
    @ConditionalOnMissingBean
    public CommonErrorHandler errorHandler(KafkaTemplate<String, Object> template) {
        // 실패한 메시지를 DLT(Dead Letter Topic, 토픽명.DLT)로 전송합니다.
        // 기본 재시도 정책(BackOff)을 사용합니다.
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        return new DefaultErrorHandler(recoverer);
    }

    /**
     * JSON과 Java 객체 간의 변환을 지원하는 메시지 컨버터입니다.
     */
    @Bean
    @ConditionalOnMissingBean
    public RecordMessageConverter messageConverter() {
        return new StringJsonMessageConverter(objectMapper());
    }

    /**
     * Java 8 날짜/시간(LocalDateTime 등) 타입을 지원하는 ObjectMapper를 생성합니다.
     */
    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
