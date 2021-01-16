package com.guigehling.voting.service;

import com.guigehling.voting.dto.PautaDTO;
import com.guigehling.voting.entity.Pauta;
import com.guigehling.voting.repository.PautaRepository;
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
public class PautaService {

    private final PautaRepository pautaRepository;

    public Page<PautaDTO> getPautas(final Pageable pageable) {
        return pautaRepository.findAll(pageable).map(PautaService::buildPautaDTO);
    }

    private static PautaDTO buildPautaDTO(final Pauta pauta) {
        return PautaDTO.builder()
                .idPauta(pauta.getIdPauta())
                .descricao(pauta.getDescricao())
                .build();
    }

}