package com.guigehling.voting.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@JsonDeserialize(builder = PautaDTO.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
public class PautaDTO {

    Long idPauta;
    Long descricao;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {
    }
}
