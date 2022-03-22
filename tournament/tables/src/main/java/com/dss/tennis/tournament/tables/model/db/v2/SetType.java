package com.dss.tennis.tournament.tables.model.db.v2;

public enum SetType {
    SET_ONE("setOne"),
    SET_TWO("setTwo"),
    SET_THREE("setThree"),
    TIE_BREAK("tieBreak");

    public final String value;

    SetType(String value) {
        this.value = value;
    }
}
