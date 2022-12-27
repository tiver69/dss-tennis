package ua.com.dss.tennis.tournament.api.model.db.v2;

@Deprecated
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
