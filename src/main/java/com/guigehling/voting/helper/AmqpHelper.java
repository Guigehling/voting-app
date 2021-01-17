package com.guigehling.voting.helper;

import lombok.Getter;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Getter
public class AmqpHelper {

    private final AmqpAdmin amqpAdmin;
    private final Exchange exchange;

    @Value("${rabbitmq.queue.session}")
    private String sessionQueue;
    @Value("${rabbitmq.route.session}")
    private String sessionRoute;

    @Value("${rabbitmq.route.result}")
    private String resultRoute;
    @Value("${rabbitmq.queue.result}")
    private String resultqueue;

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

