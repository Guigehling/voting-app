package com.guigehling.voting.repository;

import com.guigehling.voting.entity.Pauta;
import com.guigehling.voting.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Voto, Long>, JpaSpecificationExecutor<Pauta> {

    List<Voto> findByIdPauta(Long idAgenda);

    Optional<Voto> findFirstByIdPautaAndCpf(Long idAgenda, String cpf);

}