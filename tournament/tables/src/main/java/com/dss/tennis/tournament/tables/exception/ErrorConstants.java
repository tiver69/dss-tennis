package com.dss.tennis.tournament.tables.exception;

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
    CONTEST_NOT_FOUND,
    CONTEST_SCORE_EXISTS,
    CONTEST_SCORE_NOT_FOUND,
    GAME_LIMIT_EXCEEDED,
    SET_SCORE_DUPLICATION,
    SET_SCORE_EMPTY,
    PARTICIPANT_ONE_SCORE_EMPTY,
    PARTICIPANT_TWO_SCORE_EMPTY,
    SET_SCORE_NOT_FOUND,
    PARTICIPANT_ONE_TECH_DEFEAT_EMPTY,
    PARTICIPANT_TWO_TECH_DEFEAT_EMPTY,

    //participant errors
    PLAYER_FIRST_NAME_EMPTY,
    PLAYER_LAST_NAME_EMPTY,
    PLAYER_NOT_FOUND,
    PLAYER_DUPLICATION,

    //participant warnings
    PARTICIPANT_DUPLICATION,
    PLAYER_DUPLICATION_IN_TOURNAMENT,
    TEAM_NOT_FOUND,

    //common errors
    RESOURCE_OBJECT_EMPTY,
    RESOURCE_OBJECT_ID_EMPTY,
    RESOURCE_OBJECT_ATTRIBUTES_EMPTY,
    RESOURCE_OBJECT_TYPE_FORBIDDEN,
    UNSUPPORTED_RESOURCE_TYPE,
    GENERAL_BAD_REQUEST,
    INTERNAL_SERVER_ERROR,

    //common warnings,
    REQUEST_PARAMETER_NOT_ALLOWED,
    PAGE_OUT_OF_LOWER_RANGE,
    PAGE_OUT_OF_UPPER_RANGE,
    PAGE_SIZE_OUT_OF_RANGE
}
