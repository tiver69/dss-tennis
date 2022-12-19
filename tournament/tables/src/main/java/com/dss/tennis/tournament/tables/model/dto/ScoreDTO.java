package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.validator.anotation.Required;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDTO {

    private int participantOneScoreId;
    private int participantTwoScoreId;

    private SetScoreDTO setOne;
    private SetScoreDTO setTwo;
    private SetScoreDTO setThree;
    private SetScoreDTO tieBreak;
    private TechDefeatDTO techDefeat;

    public boolean isSetOneScoreNotDefined() {
        return !isSetOneScoreDefined();
    }

    public boolean isSetOneScoreDefined() {
        return setOne != null && setOne.isSetScoreDefined();
    }

    public boolean isSetTwoScoreNotDefined() {
        return !isSetTwoScoreDefined();
    }

    public boolean isSetTwoScoreDefined() {
        return setTwo != null && setTwo.isSetScoreDefined();
    }

    public boolean isSetThreeScoreNotDefined() {
        return !isSetThreeScoreDefined();
    }

    public boolean isSetThreeScoreDefined() {
        return setThree != null && setThree.isSetScoreDefined();
    }

    public boolean isTieBreakScoreNotDefined() {
        return !isTieBreakScoreDefined();
    }

    public boolean isTieBreakScoreDefined() {
        return tieBreak != null && tieBreak.isSetScoreDefined();
    }

    public boolean isScoreNotDefined() {
        return isSetOneScoreNotDefined() && isSetTwoScoreNotDefined()
                && isSetThreeScoreNotDefined() && isTieBreakScoreNotDefined();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SetScoreDTO {
        private Integer id;

        @Required(message = "PARTICIPANT_ONE_SCORE_EMPTY")
        private Byte participantOneScore;

        @Required(message = "PARTICIPANT_TWO_SCORE_EMPTY")
        private Byte participantTwoScore;

        public boolean isSetScoreNotDefined() {
            return ((participantOneScore == null || participantTwoScore == null) ||
                    (participantOneScore == 0 && participantTwoScore == 0));
        }

        public boolean isSetScoreDefined() {
            return ((participantOneScore != null && participantTwoScore != null) &&
                    (participantOneScore != 0 && participantTwoScore != 0));
        }
    }
}
