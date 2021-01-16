package com.guigehling.voting.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoEnum {

    YES("Sim"),
    NO("Não");

    private final String value;

}