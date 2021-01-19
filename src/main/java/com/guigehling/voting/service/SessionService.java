package com.guigehling.voting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guigehling.voting.config.AmqpConfig;
import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.entity.Sessao;
import com.guigehling.voting.repository.SessionRepository;
import com.guigehling.voting.util.AmqpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.lang.Boolean.TRUE;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class SessionService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private final SessionRepository sessionRepository;
    private final AgendaService agendaService;
    private final ObjectMapper objectMapper;
    private final AmqpConfig amqpConfig;
    private final AmqpUtil amqpUtil;
    private final Exchange exchange;

    public SessionDTO openVotingSession(Long idAgenda, Long minutesLong) {
        var createdDate = LocalDateTime.now(ZONE_ID);

        var sessionEntity = sessionRepository.save(Sessao.builder()
                .idPauta(idAgenda)
                .dataAbertura(createdDate)
                .dataEncerramento(createdDate.plusMinutes(minutesLong))
                .status(TRUE)
                .build());

        amqpUtil.sendMessage(exchange.getName(), amqpConfig.getSessionRoute(), sessionEntity.getIdSessao());
        return buildSessionDTO(sessionEntity);
    }

    public SessionDTO closeSession(Long idSession) {
        var sessionEntity = sessionRepository.findById(idSession).orElseThrow();

        if (validateSessionDate(sessionEntity.getDataEncerramento()))
            return buildSessionDTO(sessionEntity);

        return buildSessionDTO(sessionRepository.save(sessionEntity.withStatus(false)));
    }

    public void processMessage(Message message) {
        try {
            var idSession = objectMapper.readValue(message.getBody(), Long.TYPE);
            var session = getSession(idSession);

            if (session.getStatus()) {
                amqpUtil.sendDelayedMessage(exchange.getName(), amqpConfig.getSessionRoute(), idSession);
            } else {
                publishVotingResult(session);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MessageConversionException(e.getMessage());
        }
    }

    private static boolean validateSessionDate(LocalDateTime closingDate) {
        return closingDate.compareTo(LocalDateTime.now(ZONE_ID)) > 0;
    }

    private SessionDTO getSession(Long idSession) {
        return closeSession(idSession);
    }

    private void publishVotingResult(SessionDTO sessionDTO) {
        var agendaDetail = agendaService.getAgendaDetails(sessionDTO.getIdAgenda());
        amqpUtil.sendMessage(exchange.getName(), amqpConfig.getResultRoute(), agendaDetail);
    }

    private static SessionDTO buildSessionDTO(final Sessao session) {
        return SessionDTO.builder()
                .idSession(session.getIdSessao())
                .idAgenda(session.getIdPauta())
                .openingDate(session.getDataAbertura())
                .closingDate(session.getDataEncerramento())
                .status(session.getStatus())
                .build();
    }

}