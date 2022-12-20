package com.dss.tennis.tournament.tables.model.definitions.contest;

import lombok.*;

@Data
@Builder
public class ContestAttributes {

    private TechDefeat techDefeat;
    private ContestAttributesScore score;

    @Getter
    @Setter
    @Builder
    public static class ContestAttributesScore {
        private ContestAttributesSetScore setOne;
        private ContestAttributesSetScore setTwo;
        private ContestAttributesSetScore setThree;
        private ContestAttributesSetScore tieBreak;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public final static class ContestAttributesSetScore {
        private Byte participantOne;
        private Byte participantTwo;
    }
}
