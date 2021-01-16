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
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVoto;
    private Long idPauta;
    @Enumerated(EnumType.STRING)
    private YesNoEnum voto;
    private String cpf;

}
