package com.guigehling.voting.service;

import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.dto.VoteDTO;
import com.guigehling.voting.entity.Sessao;
import com.guigehling.voting.entity.Voto;
import com.guigehling.voting.exception.BusinessException;
import com.guigehling.voting.repository.SessionRepository;
import com.guigehling.voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class VoteService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private final VoteRepository voteRepository;
    private final SessionRepository sessionRepository;

    public VoteDTO registerVote(VoteDTO voteDTO) {
        if (hasVoted(voteDTO))
            throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,
                    String.format("CPF %s já votou na pauta %s.", voteDTO.getCpf(), voteDTO.getIdAgenda()));

        if (!validateSession(voteDTO.getIdAgenda()))
            throw new BusinessException(HttpStatus.NOT_ACCEPTABLE,
                    String.format("Não a sessão ativa para a pauta %s.", voteDTO.getIdAgenda()));

        var vote = voteRepository.save(buildVote(voteDTO));
        return voteDTO.withIdVote(vote.getIdVoto());
    }

    private boolean hasVoted(VoteDTO voteDTO) {
        var optVote = voteRepository.findFirstByIdPautaAndCpf(voteDTO.getIdAgenda(), voteDTO.getCpf());
        return optVote.isPresent();
    }

    private boolean validateSession(Long idAgenda) {
        var session = sessionRepository.findByIdPauta(idAgenda);

        var optSessionDTO = session.stream()
                .filter(this::filterOpenSessions)
                .map(this::buildSessionDTO)
                .findAny();

        return optSessionDTO.filter(sessionDTO -> validateSessionDate(sessionDTO.getClosingDate())).isPresent();
    }

    private static boolean validateSessionDate(LocalDateTime closingDate) {
        return closingDate.compareTo(LocalDateTime.now(ZONE_ID)) > 0;
    }

    private boolean filterOpenSessions(Sessao sessao) {
        return sessao.getStatus();
    }

    private Voto buildVote(VoteDTO voteDTO) {
        return Voto.builder()
                .idPauta(voteDTO.getIdAgenda())
                .cpf(voteDTO.getCpf())
                .voto(voteDTO.getVote())
                .build();
    }

    private SessionDTO buildSessionDTO(Sessao session) {
        return SessionDTO.builder()
                .idSession(session.getIdSessao())
                .idAgenda(session.getIdPauta())
                .openingDate(session.getDataAbertura())
                .closingDate(session.getDataEncerramento())
                .status(session.getStatus())
                .build();
    }

}