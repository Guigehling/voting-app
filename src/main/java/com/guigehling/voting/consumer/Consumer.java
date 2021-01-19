package com.guigehling.voting.consumer;

import com.guigehling.voting.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Consumer {

    private final SessionService sessionService;

    @RabbitListener(queues = {"${spring.rabbitmq.session.queue}"})
    public void receiveSessionMessage(Message message) {
        sessionService.processMessage(message);
    }

}
