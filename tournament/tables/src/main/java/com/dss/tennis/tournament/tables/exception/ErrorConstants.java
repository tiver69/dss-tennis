package com.dss.tennis.tournament.tables.exception;

public interface ErrorConstants {

    enum ErrorKey {
        //tournament errors
        TOURNAMENT_NAME_EMPTY,
        TOURNAMENT_NAME_DUPLICATE,
        TOURNAMENT_TYPE_EMPTY,
        PARTICIPANT_TYPE_EMPTY,
        PARTICIPANT_TYPE_UPDATE_FORBIDDEN,
        TOURNAMENT_TYPE_NOT_SUPPORTED,
        TOURNAMENT_TYPE_UPDATE_FORBIDDEN,
        BEGINNING_DATE_UPDATE_FORBIDDEN,
        TOURNAMENT_NOT_FOUND,
        FORBIDDEN_PARTICIPANT_QUANTITY,
        FORBIDDEN_PARTICIPANT_QUANTITY_REMOVING,

        //contest errors
        CONTEST_NOT_FOUND,
        CONTEST_SCORE_EXISTS,
        CONTEST_UPDATE_ATTRIBUTES_EMPTY,
        CONTEST_SCORE_NOT_FOUND,
        GAME_LIMIT_EXCEEDED,
        SET_SCORE_DUPLICATION,
        SET_SCORE_EMPTY,
        PARTICIPANT_ONE_SCORE_EMPTY,
        PARTICIPANT_TWO_SCORE_EMPTY,
        SET_SCORE_NOT_FOUND,
        PARTICIPANT_ONE_TECH_DEFEAT_EMPTY,
        PARTICIPANT_TWO_TECH_DEFEAT_EMPTY,
        CONTEST_NOT_REACHED,
        CONTEST_SCORE_UPDATE_FORBIDDEN,
        CONTEST_FULL_TECH_DEFEAT_FORBIDDEN,

        //participant errors
        PLAYER_FIRST_NAME_EMPTY,
        PLAYER_LAST_NAME_EMPTY,
        PLAYER_ONE_EMPTY,
        PLAYER_TWO_EMPTY,
        PLAYER_NOT_FOUND,
        TEAM_NOT_FOUND,
        PARTICIPANT_NOT_FOUND,
        PLAYER_DUPLICATION,
        TEAM_PLAYER_DUPLICATION,
        TEAM_PLAYER_NOT_FOUND,
        TEAM_DUPLICATION,
        BIRTH_DATE_ILLEGAL,
        EXPERIENCE_YEAR_ILLEGAL,

        //participant warnings
        PARTICIPANT_DUPLICATION,
        PLAYER_DUPLICATION_IN_TOURNAMENT,

        //common errors
        RESOURCE_OBJECT_EMPTY,
        RESOURCE_OBJECT_ID_EMPTY,
        RESOURCE_OBJECT_ATTRIBUTES_EMPTY,
        RESOURCE_OBJECT_TYPE_FORBIDDEN,
        UNSUPPORTED_RESOURCE_TYPE,
        GENERAL_BAD_REQUEST,
        INTERNAL_SERVER_ERROR,
        UNAUTHORIZED,
        AUTHENTICATION_FAILED,
        ACCOUNT_LOCKED,

        //common warnings,
        PAGE_OUT_OF_LOWER_RANGE,
        PAGE_OUT_OF_UPPER_RANGE,
        PAGE_SIZE_OUT_OF_RANGE
    }

    enum ErrorPointerKey {
        SET_ONE("setOne"),
        SET_TWO("setTwo"),
        SET_THREE("setThree"),
        TIE_BREAK("tieBreak");

        public final String value;

        ErrorPointerKey(String value) {
            this.value = value;
        }
    }
}
