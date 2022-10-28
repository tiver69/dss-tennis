package com.dss.tennis.tournament.tables.model.definitions.contest;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ContestRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateContestScoreRequest {
        private int id;
        private final String type = ResourceObjectType.CONTEST.value;
        private ContestAttributes attributes;
    }
}
