package com.guigehling.voting.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@With
@Value
@JsonDeserialize(builder = AgendaDTO.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
public class AgendaDTO {

    Long idAgenda;
    @NotNull @NotBlank
    String description;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {
    }
}
