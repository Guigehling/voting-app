package com.guigehling.voting.repository;

import com.guigehling.voting.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgendaRepository extends JpaRepository<Pauta, Long>, JpaSpecificationExecutor<Pauta> {

}