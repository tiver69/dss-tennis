package com.dss.tennis.tournament.tables.model.dto;

import com.dss.tennis.tournament.tables.validator.constraint.anotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechDefeatDTO {

    @Required(message = "PARTICIPANT_ONE_TECH_DEFEAT_EMPTY")
    private Boolean participantOne;

    @Required(message = "PARTICIPANT_TWO_TECH_DEFEAT_EMPTY")
    private Boolean participantTwo;

    public boolean isTechDefeat() {
        return BooleanUtils.isTrue(participantOne) || BooleanUtils.isTrue(participantTwo);
    }
}
