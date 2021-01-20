package com.guigehling.voting.repository;

import com.guigehling.voting.entity.Agenda;
import com.guigehling.voting.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>, JpaSpecificationExecutor<Agenda> {

    List<Session> findByIdAgenda(Long idAgenda);

}