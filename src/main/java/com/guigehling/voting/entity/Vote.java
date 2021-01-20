package com.guigehling.voting.entity;

import com.guigehling.voting.enumeration.YesNoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "voto", schema = "dbo")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voto")
    private Long idVote;

    @Column(name = "id_pauta")
    private Long idAgenda;

    @Enumerated(EnumType.STRING)
    @Column(name = "voto")
    private YesNoEnum vote;

    private String cpf;

}
