package com.guigehling.voting.service;

import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.entity.Sessao;
import com.guigehling.voting.helper.AmqpHelper;
import com.guigehling.voting.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final RabbitTemplate rabbitTemplate;
    private final AmqpHelper amqpHelper;
    private final Exchange exchange;

    public SessionDTO openVotingSession(Long idAgenda, Long minutesLong) {
        var createdDate = LocalDateTime.now(ZONE_ID);

        var sessionEntity = sessionRepository.save(Sessao.builder()
                .idPauta(idAgenda)
                .dataAbertura(createdDate)
                .dataEncerramento(createdDate.plusMinutes(minutesLong))
                .status(TRUE)
                .build());

        sendMessage(sessionEntity.getIdSessao());

        return buildSessionDTO(sessionEntity);
    }

    public SessionDTO closeSession(Long idSession) {
        var sessionEntity = sessionRepository.findById(idSession).orElseThrow();

        if (validateSessionDate(sessionEntity.getDataEncerramento()))
            return buildSessionDTO(sessionEntity);

        return buildSessionDTO(sessionRepository.save(sessionEntity.withStatus(false)));
    }

    private static boolean validateSessionDate(LocalDateTime closingDate) {
        return closingDate.compareTo(LocalDateTime.now(ZONE_ID)) > 0;
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

    public void sendMessage(Long idSession) {
        rabbitTemplate.convertAndSend(exchange.getName(), amqpHelper.getSessionRoute(), idSession);
    }

}