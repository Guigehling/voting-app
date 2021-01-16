package com.guigehling.voting.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
@Table(name = "voto", schema = "dbo")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idVoto;
    Long idPauta;
//    @Enumerated(EnumType.STRING)
//    private PersonTypeEnum type;
    String voto;
    String cpf;

}
