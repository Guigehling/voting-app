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
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSessao;
    private Long idPauta;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataEncerramento;
    private Boolean status;

}
