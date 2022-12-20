package com.dss.tennis.tournament.tables.converter.request;

import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes.ContestAttributesScore;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes.ContestAttributesSetScore;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestRequest.UpdateContestScoreRequest;
import com.dss.tennis.tournament.tables.model.definitions.contest.TechDefeat;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.ScoreDTOBuilder;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@Getter
@Setter
public class UpdateContestScoreRequestToScoreDtoConverter implements Converter<UpdateContestScoreRequest, ScoreDTO> {

    @Override
    public ScoreDTO convert(MappingContext<UpdateContestScoreRequest, ScoreDTO> context) {
        ContestAttributes updateScoreAttributes = context.getSource().getAttributes();
        if (updateScoreAttributes == null) return convertEmptyScoreDto();

        ContestAttributesScore updateScore = updateScoreAttributes.getScore();
        ScoreDTOBuilder builder = updateScore != null ?
                ScoreDTO.builder()
                        .setOne(convertSetScoreDto(updateScore.getSetOne()))
                        .setTwo(convertSetScoreDto(updateScore.getSetTwo()))
                        .setThree(convertSetScoreDto(updateScore.getSetThree()))
                        .tieBreak(convertSetScoreDto(updateScore.getTieBreak())) :
                ScoreDTO.builder();

        return builder.techDefeat(convertTechDefeat(updateScoreAttributes.getTechDefeat())).build();
    }

    private ScoreDTO convertEmptyScoreDto() {
        return ScoreDTO.builder()
                .setOne(null)
                .setTwo(null)
                .setThree(null)
                .tieBreak(null)
                .techDefeat(null)
                .build();
    }

    private SetScoreDTO convertSetScoreDto(ContestAttributesSetScore contestSetScore) {
        return contestSetScore == null ? null : SetScoreDTO.builder()
                .participantOneScore(contestSetScore.getParticipantOne())
                .participantTwoScore(contestSetScore.getParticipantTwo())
                .build();
    }

    private TechDefeatDTO convertTechDefeat(TechDefeat techDefeat) {
        return techDefeat == null ? null : new TechDefeatDTO(techDefeat.getParticipantOne(), techDefeat
                .getParticipantTwo());
    }
}