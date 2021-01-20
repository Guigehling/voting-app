package com.guigehling.voting.repository;

import com.guigehling.voting.entity.Agenda;
import com.guigehling.voting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>, JpaSpecificationExecutor<Agenda> {

    List<Vote> findByIdAgenda(Long idAgenda);

    Optional<Vote> findFirstByIdAgendaAndCpf(Long idAgenda, String cpf);

}