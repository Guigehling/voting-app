package com.guigehling.voting.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guigehling.voting.dto.AgendaDetailsDTO;
import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.helper.AmqpHelper;
import com.guigehling.voting.service.AgendaService;
import com.guigehling.voting.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SessionConsumer {

    private final Exchange exchange;
    private final AmqpHelper amqpHelper;
    //    private final AmqpConfig amqpConfig;
    private final ObjectMapper objectMapper;
    private final SessionService sessionService;
    private final RabbitTemplate rabbitTemplate;
    private final AgendaService agendaService;

    @RabbitListener(queues = {"${spring.rabbitmq.session.queue}"})
    public void receiveMessage(Message message) {
        onMessage(message);
    }

    private void onMessage(Message message) {
        try {
            var idSession = objectMapper.readValue(message.getBody(), Long.TYPE);
            var session = getSession(idSession);

            if (session.getStatus()) {
                resendMessage(idSession);
            } else {
                publishVotingResult(session);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MessageConversionException(e.getMessage());
        }
    }

    private SessionDTO getSession(Long idSession) {
        return sessionService.closeSession(idSession);
    }

    private void publishVotingResult(SessionDTO sessionDTO) {
        var agendaDetail = agendaService.getAgendaDetails(sessionDTO.getIdAgenda());
        sendMessage(agendaDetail);
    }

    public void sendMessage(AgendaDetailsDTO agendaDetailsDTO) {
        rabbitTemplate.convertAndSend(exchange.getName(), amqpHelper.getResultRoute(), agendaDetailsDTO);
    }

    @Async
    public void resendMessage(Long idSession) {
        try {
            //FIXME - A ideia aqui é de utilizar um plugin de delay do Rabbit, porém não consegui fazer isto em um
            // servidor gratuito, então executiei esta solução a custo zero, mas reconheço não ser a melhor.
            Thread.sleep(amqpHelper.getDelayDefault());

            var properties = new MessageProperties();
            properties.setHeader("x-delay", amqpHelper.getDelayDefault());

            var mapper = new ObjectMapper();
            var message = MessageBuilder.withBody(mapper.writeValueAsBytes(idSession))
                    .andProperties(properties)
                    .build();

            rabbitTemplate.convertAndSend(exchange.getName(), amqpHelper.getSessionRoute(), message);
        } catch (Exception e) {
            log.error(String.format("Erro ao enfileirar novamente sessão id %s", idSession), e);
        }
    }
}
