package com.guigehling.voting.service;

import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.dto.VoteDTO;
import com.guigehling.voting.entity.Session;
import com.guigehling.voting.entity.Vote;
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

    public void registerVote(VoteDTO voteDTO) {
        if (validateVotingRequirements(voteDTO))
            voteRepository.save(buildVote(voteDTO));
    }

    private boolean validateVotingRequirements(VoteDTO voteDTO) {
        if (validateHasVoted(voteDTO))
            throw new BusinessException(PRECONDITION_FAILED, messageHelper.get(ERROR_VOTE_USER_VOTED, voteDTO.getCpf(), voteDTO.getIdAgenda()));

        if (validateSessionIsClosed(voteDTO.getIdAgenda()))
            throw new BusinessException(PRECONDITION_FAILED, messageHelper.get(ERROR_VOTE_SESSION_CLOSED, voteDTO.getIdAgenda()));

        if (validateIsUnable(voteDTO.getCpf()))
            throw new BusinessException(PRECONDITION_FAILED, messageHelper.get(ERROR_VOTE_USER_UNABLE, voteDTO.getCpf()));

        return true;
    }

    private boolean validateHasVoted(VoteDTO voteDTO) {
        var optVote = voteRepository.findFirstByIdAgendaAndCpf(voteDTO.getIdAgenda(), voteDTO.getCpf());
        return optVote.isPresent();
    }

    private boolean validateSessionIsClosed(Long idAgenda) {
        var session = sessionRepository.findByIdAgenda(idAgenda);

        var optSessionDTO = session.stream()
                .filter(this::filterOpenSessions)
                .map(this::buildSessionDTO)
                .findAny();

        return optSessionDTO.filter(sessionDTO -> validateSessionDate(sessionDTO.getClosingDate())).isEmpty();
    }

    private static boolean validateSessionDate(LocalDateTime closingDate) {
        return closingDate.compareTo(LocalDateTime.now(ZONE_ID)) > 0;
    }

    private boolean validateIsUnable(String cpf) {
        var result = userIntegration.isAble(cpf);
        return result.getStatus().equals(UNABLE_TO_VOTE);
    }

    private boolean filterOpenSessions(Session session) {
        return session.getStatus();
    }

    private Vote buildVote(VoteDTO voteDTO) {
        return Vote.builder()
                .idAgenda(voteDTO.getIdAgenda())
                .cpf(voteDTO.getCpf())
                .vote(voteDTO.getVote())
                .build();
    }

    private SessionDTO buildSessionDTO(Session session) {
        return SessionDTO.builder()
                .idSession(session.getIdSession())
                .idAgenda(session.getIdAgenda())
                .openingDate(session.getOpeningDate())
                .closingDate(session.getClosingDate())
                .status(session.getStatus())
                .build();
    }

}