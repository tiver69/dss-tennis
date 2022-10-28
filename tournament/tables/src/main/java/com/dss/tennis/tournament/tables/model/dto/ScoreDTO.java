package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.validator.anotation.Required;
import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDTO {

    private TechDefeatDTO techDefeat;
    private Map<SetType, SetScoreDTO> sets;

    public boolean isSetScoreNotDefined(SetType type) {
        return sets == null || sets.get(type).isSetScoreNotDefined();
    }

    public boolean isSetScoreDefined(SetType type) {
        return sets != null && sets.get(type).isSetScoreDefined();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetScoreDTO {
        private Integer id;

        @Required(message = "PARTICIPANT_ONE_SCORE_EMPTY")
        private Byte participantOneScore;

        @Required(message = "PARTICIPANT_TWO_SCORE_EMPTY")
        private Byte participantTwoScore;

        public SetScoreDTO(Integer id) {
            this.id = id;
        }

        public SetScoreDTO(Byte participantOneScore, Byte participantTwoScore) {
            this.participantOneScore = participantOneScore;
            this.participantTwoScore = participantTwoScore;
        }

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
