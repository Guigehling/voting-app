package com.guigehling.voting.integration.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.guigehling.voting.integration.user.enumeration.UserStatusEnum;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@JsonDeserialize(builder = UserStatus.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
public class UserStatus {

    UserStatusEnum status;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {
    }
}
