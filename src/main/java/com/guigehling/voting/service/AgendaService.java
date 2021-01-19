package com.guigehling.voting.service;

import com.guigehling.voting.dto.AgendaDTO;
import com.guigehling.voting.dto.AgendaDetailsDTO;
import com.guigehling.voting.entity.Pauta;
import com.guigehling.voting.entity.Voto;
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
        var votes = voteRepository.findByIdPauta(agendaDetailsDTO.getIdAgenda());

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

    private boolean filterInFavourVotes(Voto voto) {
        return voto.getVoto().equals(YesNoEnum.YES);
    }

    private boolean filterAgainstVotes(Voto voto) {
        return voto.getVoto().equals(YesNoEnum.NO);
    }

    private static Pauta buildPauta(final AgendaDTO agendaDTO) {
        return Pauta.builder()
                .descricao(agendaDTO.getDescription())
                .build();
    }

    private static AgendaDTO buildAgendaDTO(final Pauta pauta) {
        return AgendaDTO.builder()
                .idAgenda(pauta.getIdPauta())
                .description(pauta.getDescricao())
                .build();
    }

    private static AgendaDetailsDTO buildAgendaDetailsDTO(final Pauta pauta) {
        return AgendaDetailsDTO.builder()
                .idAgenda(pauta.getIdPauta())
                .description(pauta.getDescricao())
                .build();
    }

}