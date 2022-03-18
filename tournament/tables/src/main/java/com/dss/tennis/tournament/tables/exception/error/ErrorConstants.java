package com.dss.tennis.tournament.tables.exception.error;

public enum ErrorConstants {

    //tournament errors
    TOURNAMENT_NAME_EMPTY,
    TOURNAMENT_NAME_DUPLICATE,
    TOURNAMENT_TYPE_EMPTY,
    PARTICIPANT_TYPE_EMPTY,
    TOURNAMENT_TYPE_NOT_SUPPORTED,
    TOURNAMENT_NOT_FOUND,
    FORBIDDEN_PARTICIPANT_QUANTITY,

    //contest errors

    //player errors
    PLAYER_FIRST_NAME_EMPTY,
    PLAYER_LAST_NAME_EMPTY,
    PLAYER_NOT_FOUND,
    PLAYER_DUPLICATION,

    //common errors
    UNSUPPORTED_RESOURCE_TYPE,
    GENERAL_BAD_REQUEST,
    INTERNAL_SERVER_ERROR
}
