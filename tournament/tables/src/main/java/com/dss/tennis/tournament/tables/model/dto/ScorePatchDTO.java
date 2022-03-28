package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScorePatchDTO {

    private Map<SetType, SetScorePatchDTO> sets;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetScorePatchDTO extends SetScoreDTO {
        private boolean isDeletePatch;
        private boolean isCreatePatch;

        public SetScorePatchDTO(SetScoreDTO setScore) {
            this(setScore.getId(), setScore.getParticipantOneScore(), setScore.getParticipantTwoScore());
        }

        public SetScorePatchDTO(Integer id, Byte participantOneScore, Byte participantTwoScore) {
            super(id, participantOneScore, participantTwoScore);
            if (participantOneScore == null && participantTwoScore == null) isDeletePatch = true;
            if (id == null) isCreatePatch = true;
        }
    }
}
