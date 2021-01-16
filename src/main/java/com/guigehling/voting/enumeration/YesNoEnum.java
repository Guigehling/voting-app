package com.guigehling.voting.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoEnum {

    @JsonProperty("yes")
    YES("Sim"),
    @JsonProperty("no")
    NO("Não");

    private final String value;

}