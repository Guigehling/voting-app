package com.guigehling.voting.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
@Table(name = "pauta", schema = "voto")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idVoto;
    Long idPauta;
//    @Enumerated(EnumType.STRING)
//    private PersonTypeEnum type;
//    String voto;

}
