package com.guigehling.voting.repository;

import com.guigehling.voting.entity.Pauta;
import com.guigehling.voting.entity.Sessao;
import com.guigehling.voting.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VoteRepository extends JpaRepository<Voto, Long>, JpaSpecificationExecutor<Pauta> {

}