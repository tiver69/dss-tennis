package com.dss.tennis.tournament.tables.model.definitions.contest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.CONTEST;

public class ContestRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateContestScoreRequest {
        private int id;
        private final String type = CONTEST.value;
        private ContestAttributes attributes;
    }
}
