package com.guigehling.voting.service;

import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.dto.VoteDTO;
import com.guigehling.voting.entity.Sessao;
import com.guigehling.voting.entity.Voto;
import com.guigehling.voting.exception.BusinessException;
import com.guigehling.voting.helper.MessageHelper;
import com.guigehling.voting.integration.user.UserIntegration;
import com.guigehling.voting.repository.SessionRepository;
import com.guigehling.voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.guigehling.voting.enumeration.ErrorCodeEnum.*;
import static com.guigehling.voting.integration.user.enumeration.UserStatusEnum.UNABLE_TO_VOTE;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class VoteService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private final MessageHelper messageHelper;
    private final VoteRepository voteRepository;
    private final UserIntegration userIntegration;
    private final SessionRepository sessionRepository;

    public void registerVote(VoteDTO voteDTO) throws BusinessException {
        if (check(voteDTO))
            voteRepository.save(buildVote(voteDTO));
    }

    private boolean check(VoteDTO voteDTO) {
        if (validateHasVoted(voteDTO))
            throw new BusinessException(PRECONDITION_FAILED, messageHelper.get(ERROR_VOTE_USER_VOTED, voteDTO.getCpf(), voteDTO.getIdAgenda()));

        if (validateSessionIsClosed(voteDTO.getIdAgenda()))
            throw new BusinessException(PRECONDITION_FAILED, messageHelper.get(ERROR_VOTE_SESSION_CLOSED, voteDTO.getIdAgenda()));

        if (validateIsUnable(voteDTO.getCpf()))
            throw new BusinessException(PRECONDITION_FAILED, messageHelper.get(ERROR_VOTE_USER_UNABLE, voteDTO.getCpf()));

        return true;
    }

    private boolean validateHasVoted(VoteDTO voteDTO) {
        var optVote = voteRepository.findFirstByIdPautaAndCpf(voteDTO.getIdAgenda(), voteDTO.getCpf());
        return optVote.isPresent();
    }

    private boolean validateSessionIsClosed(Long idAgenda) {
        var session = sessionRepository.findByIdPauta(idAgenda);

        var optSessionDTO = session.stream()
                .filter(this::filterOpenSessions)
                .map(this::buildSessionDTO)
                .findAny();

        return optSessionDTO.filter(sessionDTO -> validateSessionDate(sessionDTO.getClosingDate())).isEmpty();
    }

    private boolean filterOpenSessions(Sessao sessao) {
        return sessao.getStatus();
    }

    private static boolean validateSessionDate(LocalDateTime closingDate) {
        return closingDate.compareTo(LocalDateTime.now(ZONE_ID)) > 0;
    }

    private boolean validateIsUnable(String cpf) {
        var result = userIntegration.isAble(cpf);
        return result.getStatus().equals(UNABLE_TO_VOTE);
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