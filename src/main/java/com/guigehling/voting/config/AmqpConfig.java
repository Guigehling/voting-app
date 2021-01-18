package com.guigehling.voting.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;

@Getter
@Configuration
public class AmqpConfig {

    @Value("${spring.rabbitmq.voting.direct.exchange}")
    private String votingDirectExchange;
    @Value("${spring.rabbitmq.voting.direct.fanout}")
    private String votingFanoutExchange;

    @Bean
    @Primary
    Exchange createDirectExchange() {
        var args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");

        return ExchangeBuilder
                .directExchange(votingDirectExchange)
                .durable(true)
                .withArguments(args)
                .build();
    }

//    @Bean
//    Exchange createFanoutExchange() {
//        return ExchangeBuilder
//                .fanoutExchange(votingFanoutExchange)
//                .durable(true)
//                .build();
//    }

    @Bean
    Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

}
