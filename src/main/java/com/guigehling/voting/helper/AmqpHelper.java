package com.guigehling.voting.helper;

import lombok.Getter;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
public class AmqpHelper {

    private final AmqpAdmin amqpAdmin;
    private final Exchange exchange;

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

    public AmqpHelper(AmqpAdmin amqpAdmin, Exchange exchange) {
        this.amqpAdmin = amqpAdmin;
        this.exchange = exchange;
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
                        .to(exchange)
                        .with(routeName)
                        .noargs());
    }
}

