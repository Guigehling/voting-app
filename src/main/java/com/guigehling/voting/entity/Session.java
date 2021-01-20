package com.guigehling.voting.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sessao", schema = "dbo")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sessao")
    private Long idSession;

    @Column(name = "id_pauta")
    private Long idAgenda;

    @Column(name = "data_abertura")
    private LocalDateTime openingDate;

    @Column(name = "data_encerramento")
    private LocalDateTime closingDate;

    private Boolean status;

}
