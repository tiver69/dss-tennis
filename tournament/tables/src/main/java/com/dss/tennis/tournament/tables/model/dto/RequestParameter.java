package com.dss.tennis.tournament.tables.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestParameter {

    public static RequestParameter DEFAULT = new RequestParameter(true, false);

    private boolean includeContests;
    private boolean includePlayers;

    public void copyRequestParameterTo(RequestParameter result) {
        result.setIncludeContests(includeContests);
        result.setIncludePlayers(includePlayers);
    }

    public void resetStateToDefault() {
        includeContests = false;
        includePlayers = false;
    }
}
