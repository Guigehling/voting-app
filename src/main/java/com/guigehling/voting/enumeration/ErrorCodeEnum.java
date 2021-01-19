package com.guigehling.voting.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodeEnum {

    ERROR_VOTE_SESSION_CLOSED("error.vote.session.closed"),
    ERROR_VOTE_USER_VOTED("error.vote.user.has.voted"),
    ERROR_VOTE_USER_UNABLE("error.vote.user.unable");

    private final String messageKey;
}
