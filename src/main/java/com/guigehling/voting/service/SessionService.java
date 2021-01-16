package com.guigehling.voting.service;

import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.entity.Sessao;
import com.guigehling.voting.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public SessionDTO openVotingSession(Long idAgenda, Long minutesLong) {
        var createdDate = LocalDateTime.now(ZONE_ID);

        var sessionEntity = sessionRepository.save(Sessao.builder()
                .idPauta(idAgenda)
                .dataAbertura(createdDate)
                .dataEncerramento(createdDate.plusMinutes(minutesLong))
                .status(TRUE)
                .build());

        return buildSessionDTO(sessionEntity);
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