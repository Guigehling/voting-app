package com.guigehling.voting.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@JsonDeserialize(builder = AgendaDetailsDTO.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
public class AgendaDetailsDTO {

    Long idAgenda;
    String description;
    Long totalVotes;
    Long totalVotesFavour;
    Long totalVotesAgainst;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {
    }
}
