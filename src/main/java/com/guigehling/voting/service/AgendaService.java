package com.guigehling.voting.service;

import com.guigehling.voting.dto.AgendaDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    public AgendaDTO getAgendaDetails(Long idAgenda) {
        var optAgenda = agendaRepository.findById(idAgenda).map(AgendaService::buildAgendaDTO);

        if (optAgenda.isPresent())
            return countAllVotes(optAgenda.get());

        throw new BusinessException(HttpStatus.NOT_FOUND, String.format("Não encotnrada pauta para o id %s.", idAgenda));
    }

    private AgendaDTO countAllVotes(AgendaDTO agendaDTO) {
        var votes = voteRepository.findByIdPauta(agendaDTO.getIdAgenda());

        var totalVotesFavour = votes.stream()
                .filter(this::filterInFavourVotes)
                .count();

        var totalVotesAgainst = votes.stream()
                .filter(this::filterAgainstVotes)
                .count();

        return agendaDTO
                .withTotalVotes((long) votes.size())
                .withTotalVotesFavour(totalVotesFavour)
                .withTotalVotesFavour(totalVotesAgainst);
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

}