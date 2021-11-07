package com.dss.tennis.tournament.tables.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestParameter {

    private boolean includeContests;

    public void copyRequestParameterTo(RequestParameter result) {
        result.setIncludeContests(includeContests);
    }

    public void resetStateToDefault() {
        includeContests = false;
    }
}
