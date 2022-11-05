package com.dss.tennis.tournament.tables.converter.v2.request;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes.ContestAttributesScore;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestRequest.UpdateContestScoreRequest;
import com.dss.tennis.tournament.tables.model.definitions.contest.TechDefeat;
import com.dss.tennis.tournament.tables.model.dto.ContestScorePatchDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UpdateContestScoreRequestToScorePatchConverter implements Converter<UpdateContestScoreRequest,
        ContestScorePatchDTO> {

    @Override
    public ContestScorePatchDTO convert(MappingContext<UpdateContestScoreRequest, ContestScorePatchDTO> context) {
        ContestAttributes updateScore = context.getSource().getAttributes();

        return ContestScorePatchDTO.builder()
                .techDefeat(convertTechDefeat(updateScore.getTechDefeat()))
                .sets(convertScorePatchSets(updateScore.getScore()))
                .build();
    }

    private TechDefeatDTO convertTechDefeat(TechDefeat techDefeat) {
        return techDefeat == null ? null : new TechDefeatDTO(techDefeat.getParticipantOne(), techDefeat
                .getParticipantTwo());
    }

    private Map<SetType, SetScoreDTO> convertScorePatchSets(ContestAttributesScore updateScore) {
        if (updateScore == null)
            return null;
        Map<SetType, SetScoreDTO> sets = new HashMap<>();
        if (updateScore.getSetOne() != null)
            sets.put(SetType.SET_ONE, new SetScoreDTO(updateScore.getSetOne().getParticipantOne(), updateScore
                    .getSetOne().getParticipantTwo()));
        if (updateScore.getSetTwo() != null)
            sets.put(SetType.SET_TWO, new SetScoreDTO(updateScore.getSetTwo().getParticipantOne(), updateScore
                    .getSetTwo().getParticipantTwo()));
        if (updateScore.getSetThree() != null)
            sets.put(SetType.SET_THREE, new SetScoreDTO(updateScore.getSetThree().getParticipantOne(), updateScore
                    .getSetThree().getParticipantTwo()));
        if (updateScore.getTieBreak() != null)
            sets.put(SetType.TIE_BREAK, new SetScoreDTO(updateScore.getTieBreak().getParticipantOne(), updateScore
                    .getTieBreak().getParticipantTwo()));
        return sets;
    }
}