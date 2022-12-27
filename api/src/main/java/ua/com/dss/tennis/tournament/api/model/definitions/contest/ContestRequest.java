package ua.com.dss.tennis.tournament.api.model.definitions.contest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.CONTEST;

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
