package com.guigehling.voting.repository;

import com.guigehling.voting.entity.Pauta;
import com.guigehling.voting.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SessionRepository extends JpaRepository<Sessao, Long>, JpaSpecificationExecutor<Pauta> {

    List<Sessao> findByIdPauta(Long idPauta);

}