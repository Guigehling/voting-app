package com.guigehling.voting.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.guigehling.voting.enumeration.YesNoEnum;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@With
@Value
@JsonDeserialize(builder = VoteDTO.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
public class VoteDTO {

    Long idVote;
    @Positive
    Long idAgenda;
    @NotNull
    YesNoEnum vote;
    @CPF
    String cpf;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {
    }
}
