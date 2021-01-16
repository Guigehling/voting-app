package com.guigehling.voting.service;

import com.guigehling.voting.dto.AgendaDTO;
import com.guigehling.voting.entity.Pauta;
import com.guigehling.voting.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaDTO create(AgendaDTO agendaDTO) {
        var pauta = agendaRepository.save(buildPauta(agendaDTO));
        return buildAgendaDTO(pauta);
    }

    public Page<AgendaDTO> query(final Pageable pageable) {
        return agendaRepository.findAll(pageable).map(AgendaService::buildAgendaDTO);
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