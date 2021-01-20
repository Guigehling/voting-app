package com.guigehling.voting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guigehling.voting.config.AmqpConfig;
import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.entity.Session;
import com.guigehling.voting.exception.BusinessException;
import com.guigehling.voting.helper.MessageHelper;
import com.guigehling.voting.repository.SessionRepository;
import com.guigehling.voting.util.AmqpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.guigehling.voting.enumeration.ErrorCodeEnum.ERROR_SESSION_OPEN;
import static com.guigehling.voting.enumeration.ErrorCodeEnum.ERROR_VOTE_SESSION_CLOSED;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class SessionService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private final SessionRepository sessionRepository;
    private final MessageHelper messageHelper;
    private final AgendaService agendaService;
    private final ObjectMapper objectMapper;
    private final AmqpConfig amqpConfig;
    private final AmqpUtil amqpUtil;

    public SessionDTO openVotingSession(Long idAgenda, Long minutesLong) {
        try {
            var createdDate = LocalDateTime.now(ZONE_ID);

            var sessionEntity = sessionRepository.save(Session.builder()
                    .idAgenda(idAgenda)
                    .openingDate(createdDate)
                    .closingDate(createdDate.plusMinutes(minutesLong))
                    .status(TRUE)
                    .build());

            amqpUtil.sendMessage(amqpConfig.getVotingDirectExchange(), amqpConfig.getSessionRoute(), sessionEntity.getIdSession());
            return buildSessionDTO(sessionEntity);
        } catch (Exception e) {
            log.error(messageHelper.get(ERROR_SESSION_OPEN, idAgenda), e.getMessage(), e);
            throw new BusinessException(INTERNAL_SERVER_ERROR, messageHelper.get(ERROR_SESSION_OPEN, idAgenda));
        }
    }

    public void processMessage(Message message) {
        try {
            var idSession = objectMapper.readValue(message.getBody(), Long.TYPE);
            var session = closeSession(idSession);

            if (session.getStatus()) {
                amqpUtil.sendDelayedMessage(amqpConfig.getVotingDirectExchange(), amqpConfig.getSessionRoute(), idSession);
            } else {
                publishVotingResult(session);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MessageConversionException(e.getMessage());
        }
    }

    public SessionDTO closeSession(Long idSession) {
        try {
            var sessionEntity = sessionRepository.findById(idSession).orElseThrow();

            if (validateSessionDate(sessionEntity.getClosingDate()))
                return buildSessionDTO(sessionEntity);

            return buildSessionDTO(sessionRepository.save(sessionEntity.withStatus(false)));
        } catch (Exception e) {
            log.error(messageHelper.get(ERROR_VOTE_SESSION_CLOSED, idSession, e.getMessage()), e);
            throw new BusinessException(INTERNAL_SERVER_ERROR, messageHelper.get(ERROR_VOTE_SESSION_CLOSED, idSession, e.getMessage()));
        }
    }

    private static boolean validateSessionDate(LocalDateTime closingDate) {
        return closingDate.compareTo(LocalDateTime.now(ZONE_ID)) > 0;
    }

    private void publishVotingResult(SessionDTO sessionDTO) {
        var agendaDetail = agendaService.getAgendaDetails(sessionDTO.getIdAgenda());
        amqpUtil.sendMessage(amqpConfig.getVotingDirectExchange(), amqpConfig.getResultRoute(), agendaDetail);
    }

    private static SessionDTO buildSessionDTO(final Session session) {
        return SessionDTO.builder()
                .idSession(session.getIdSession())
                .idAgenda(session.getIdAgenda())
                .openingDate(session.getOpeningDate())
                .closingDate(session.getClosingDate())
                .status(session.getStatus())
                .build();
    }

}