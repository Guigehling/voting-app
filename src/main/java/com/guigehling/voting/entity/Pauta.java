package com.guigehling.voting.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
@Table(name = "pauta", schema = "dbo")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPauta;
    String descricao;

}
