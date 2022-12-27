package ua.com.dss.tennis.tournament.api.model.definitions.contest;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;

@Data
public class ContestInfoResponse {

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    public static class ContestInfoResponseData implements ua.com.dss.tennis.tournament.api.model.definitions.Data {
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
