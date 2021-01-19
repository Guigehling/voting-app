package com.guigehling.voting.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Getter
@Configuration
@RequiredArgsConstructor
public class AmqpConfig {

    private final AmqpAdmin amqpAdmin;

    private Exchange exchange;

    @Value("${spring.rabbitmq.voting.direct.exchange}")
    private String votingDirectExchange;
    @Value("${spring.rabbitmq.voting.direct.fanout}")
    private String votingFanoutExchange;

    @Value("${spring.rabbitmq.session.queue}")
    private String sessionQueue;
    @Value("${spring.rabbitmq.session.route}")
    private String sessionRoute;

    @Value("${spring.rabbitmq.result.route}")
    private String resultRoute;
    @Value("${spring.rabbitmq.result.queue}")
    private String resultqueue;

    @Value("${spring.rabbitmq.delay.default}")
    private Integer delayDefault;

    @Bean
    Exchange createDirectExchange() {
        var args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");

        this.exchange = ExchangeBuilder
                .directExchange(votingDirectExchange)
                .durable(true)
                .withArguments(args)
                .build();

        return this.exchange;
    }

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

    @PostConstruct
    private void createTopology() {
        createQueueAndRoute(sessionQueue, sessionRoute);
        createQueueAndRoute(resultqueue, resultRoute);
    }

    private void createQueueAndRoute(String queueName, String routeName) {
        var queueInstance = QueueBuilder
                .durable(queueName)
                .expires(43200000) //12h
                .build();

        amqpAdmin.declareQueue(queueInstance);
        amqpAdmin.declareBinding(
                BindingBuilder.bind(queueInstance)
                        .to(this.exchange)
                        .with(routeName)
                        .noargs());
    }

}
