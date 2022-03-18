package com.dss.tennis.tournament.tables.exception.error;

public enum WarningConstant {

    //participant warnings
    PARTICIPANT_DUPLICATION,
    PLAYER_DUPLICATION_IN_TOURNAMENT,
    PLAYER_NOT_FOUND,
    TEAM_NOT_FOUND,

    //common warnings
    UNSUPPORTED_RESOURCE_TYPE,
    REQUEST_PARAMETER_NOT_ALLOWED,
    PAGE_OUT_OF_LOWER_RANGE,
    PAGE_OUT_OF_UPPER_RANGE,
    PAGE_SIZE_OUT_OF_RANGE
}
