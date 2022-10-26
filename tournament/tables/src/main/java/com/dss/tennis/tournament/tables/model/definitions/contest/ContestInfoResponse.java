package com.dss.tennis.tournament.tables.model.definitions.contest;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
public class ContestInfoResponse {

    @Getter
    @Setter
    @SuperBuilder
    public static class ContestInfoResponseData {
        private int id;
        private String type;
        private ContestInfoAttributes attributes;
        private Links links;
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class EliminationContestInfoResponseData extends ContestInfoResponseData {
        private EliminationContestInfoRelationships relationships;
    }

    @Getter
    @Setter
    @Builder
    public static class ContestInfoAttributes {
        private String participantOne;
        private String participantTwo;
        private String mainScore;
        private String tieBreak;
        private TechDefeat techDefeat;
    }

    @Getter
    @Setter
    @Builder
    public static class EliminationContestInfoRelationships {
        private SimpleResourceObject firstParentContest;
        private SimpleResourceObject secondParentContest;
    }
}
