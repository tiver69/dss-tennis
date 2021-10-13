package com.dss.tennis.tournament.tables.exception.error;

public enum ErrorConstants {

    //tournament errors
    TOURNAMENT_NAME_EMPTY,
    TOURNAMENT_NAME_DUPLICATE,
    TOURNAMENT_TYPE_NOT_SUPPORTED,
    TOURNAMENT_NOT_FOUND,

    //contest errors
    TOURNAMENT_HAS_NO_CONTESTS,

    //player errors
    PLAYER_FIRST_NAME_EMPTY,
    PLAYER_LAST_NAME_EMPTY,
    PLAYER_NOT_FOUND,

    //common errors
    INTERNAL_SERVER_ERROR
}
