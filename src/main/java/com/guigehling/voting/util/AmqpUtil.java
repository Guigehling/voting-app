package com.guigehling.voting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guigehling.voting.config.AmqpConfig;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AmqpUtil {

    private final AmqpConfig amqpConfig;
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String route, Object message) {
        rabbitTemplate.convertAndSend(exchange, route, message);
    }

    // A ideia aqui é utilizar um plugin de delay do Rabbit, porém não consegui executar em um
    // servidor gratuito, executiei esta solução a custo zero, mas reconheço não ser a melhor.
    @Async
    @SneakyThrows
    public void sendDelayedMessage(String exchange, String route, Object message) {
        Thread.sleep(amqpConfig.getDelayDefault());
        sendMessage(exchange, route, message);
    }

    // Este é o modo de publicação utilizando o plugin do agendamento/delay de mensagens.
    // https://www.rabbitmq.com/blog/2015/04/16/scheduling-messages-with-rabbitmq/
    public void sendDelayedMessageWithPlugin(String exchange, String route, Object message) throws JsonProcessingException {
        var properties = new MessageProperties();
        properties.setHeader("x-delay", amqpConfig.getDelayDefault());

        var mapper = new ObjectMapper();
        var bytesMessage = MessageBuilder.withBody(mapper.writeValueAsBytes(message))
                .andProperties(properties)
                .build();

        sendMessage(exchange, route, bytesMessage);
    }

}
