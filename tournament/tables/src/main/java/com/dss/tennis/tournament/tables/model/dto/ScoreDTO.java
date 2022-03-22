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

    private Map<SetType, SetScoreDTO> sets;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SetScoreDTO {
        private Integer id;

        @Required(message = "PARTICIPANT_ONE_SCORE_EMPTY")
        private Byte participantOneScore;

        @Required(message = "PARTICIPANT_TWO_SCORE_EMPTY")
        private Byte participantTwoScore;

        public SetScoreDTO(Byte participantOneScore, Byte participantTwoScore) {
            this.participantOneScore = participantOneScore;
            this.participantTwoScore = participantTwoScore;
        }
    }
}
