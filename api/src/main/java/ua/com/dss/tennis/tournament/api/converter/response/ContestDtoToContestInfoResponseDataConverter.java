package ua.com.dss.tennis.tournament.api.converter.response;

import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.ContestInfoAttributes;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.TechDefeat;
import ua.com.dss.tennis.tournament.api.model.dto.ScoreDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ScoreDTO.SetScoreDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TechDefeatDTO;

public class ContestDtoToContestInfoResponseDataConverter {

    protected void convertContestScoreInfoAttributes(ScoreDTO scoreDto,
                                                     ContestInfoAttributes.ContestInfoAttributesBuilder builder) {
        if (scoreDto != null) {
            String setOneScore = convertSetScoreString(scoreDto.getSetOne());
            String setTwoScore = convertSetScoreString(scoreDto.getSetTwo());
            String setThreeScore = convertSetScoreString(scoreDto.getSetThree());

            builder
                    .mainScore(setOneScore + " " + setTwoScore + " " + setThreeScore)
                    .tieBreak(scoreDto.getTieBreak().getParticipantOneScore() + ":" + scoreDto.getTieBreak()
                            .getParticipantTwoScore())
                    .techDefeat(convertTechDefeat(scoreDto.getTechDefeat()));
        }
    }

    private String convertSetScoreString(SetScoreDTO setScore) {
        return String.format("%d:%d", setScore.getParticipantOneScore(), setScore.getParticipantTwoScore());
    }

    private TechDefeat convertTechDefeat(TechDefeatDTO techDefeatDTO) {
        return new TechDefeat(techDefeatDTO.getParticipantOne(), techDefeatDTO.getParticipantTwo());
    }
}
