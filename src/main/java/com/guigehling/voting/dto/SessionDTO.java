package com.guigehling.voting.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.LocalDateTime;

@With
@Value
@JsonDeserialize(builder = SessionDTO.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
public class SessionDTO {

    Long idSession;
    Long idAgenda;
    LocalDateTime openingDate;
    LocalDateTime closingDate;
    Boolean status;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {
    }
}
