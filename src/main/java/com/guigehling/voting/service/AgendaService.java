package com.guigehling.voting.service;

import com.guigehling.voting.dto.AgendaDTO;
import com.guigehling.voting.dto.AgendaDetailsDTO;
import com.guigehling.voting.entity.Agenda;
import com.guigehling.voting.entity.Vote;
import com.guigehling.voting.enumeration.YesNoEnum;
import com.guigehling.voting.exception.BusinessException;
import com.guigehling.voting.repository.AgendaRepository;
import com.guigehling.voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final VoteRepository voteRepository;

    public AgendaDTO create(AgendaDTO agendaDTO) {
        var pauta = agendaRepository.save(buildPauta(agendaDTO));
        return buildAgendaDTO(pauta);
    }

    public Page<AgendaDTO> query(final Pageable pageable) {
        return agendaRepository.findAll(pageable).map(AgendaService::buildAgendaDTO);
    }

    public AgendaDetailsDTO getAgendaDetails(Long idAgenda) {
        var optAgenda = agendaRepository.findById(idAgenda).map(AgendaService::buildAgendaDetailsDTO);

        if (optAgenda.isPresent())
            return countAllVotes(optAgenda.get());

        throw new BusinessException(NOT_FOUND);
    }

    private AgendaDetailsDTO countAllVotes(AgendaDetailsDTO agendaDetailsDTO) {
        var votes = voteRepository.findByIdAgenda(agendaDetailsDTO.getIdAgenda());

        var totalVotesFavour = votes.stream()
                .filter(this::filterInFavourVotes)
                .count();

        var totalVotesAgainst = votes.stream()
                .filter(this::filterAgainstVotes)
                .count();

        return agendaDetailsDTO
                .withTotalVotes((long) votes.size())
                .withTotalVotesFavour(totalVotesFavour)
                .withTotalVotesAgainst(totalVotesAgainst);
    }

    private boolean filterInFavourVotes(Vote vote) {
        return vote.getVote().equals(YesNoEnum.YES);
    }

    private boolean filterAgainstVotes(Vote vote) {
        return vote.getVote().equals(YesNoEnum.NO);
    }

    private static Agenda buildPauta(final AgendaDTO agendaDTO) {
        return Agenda.builder()
                .description(agendaDTO.getDescription())
                .build();
    }

    private static AgendaDTO buildAgendaDTO(final Agenda agenda) {
        return AgendaDTO.builder()
                .idAgenda(agenda.getIdAgenda())
                .description(agenda.getDescription())
                .build();
    }

    private static AgendaDetailsDTO buildAgendaDetailsDTO(final Agenda agenda) {
        return AgendaDetailsDTO.builder()
                .idAgenda(agenda.getIdAgenda())
                .description(agenda.getDescription())
                .build();
    }

}