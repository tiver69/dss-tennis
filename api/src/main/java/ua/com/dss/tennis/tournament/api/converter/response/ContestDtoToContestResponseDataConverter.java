package ua.com.dss.tennis.tournament.api.converter.response;

import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestAttributes;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestAttributes.ContestAttributesScore;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestAttributes.ContestAttributesSetScore;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestResponseData;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestResponseData.ContestResponseDataBuilder;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.TechDefeat;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ScoreDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ScoreDTO.SetScoreDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TechDefeatDTO;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.CONTEST;

public class ContestDtoToContestResponseDataConverter {

    public ContestResponseDataBuilder getCommonContestBuilder(ContestDTO contestDTO, Integer extraTournamentId) {
        return ContestResponseData.builder()
                .id(contestDTO.getId())
                .attributes(convertContestAttributes(contestDTO.getScoreDto()))
                .links(Links.builder()
                        .self(String.format(CONTEST.selfLinkFormat, extraTournamentId, contestDTO.getId()))
                        .build());
    }

    public ContestAttributes convertContestAttributes(ScoreDTO scoreDTO) {
        return ContestAttributes.builder()
                .techDefeat(convertTechDefeat(scoreDTO.getTechDefeat()))
                .score(convertScore(scoreDTO))
                .build();
    }

    private TechDefeat convertTechDefeat(TechDefeatDTO techDefeat) {
        return new TechDefeat(techDefeat.getParticipantOne(), techDefeat.getParticipantTwo());
    }

    private ContestAttributesScore convertScore(ScoreDTO scoreDTO) {
        return ContestAttributesScore.builder()
                .setOne(convertContestAttributesSetScore(scoreDTO.getSetOne()))
                .setTwo(convertContestAttributesSetScore(scoreDTO.getSetTwo()))
                .setThree(convertContestAttributesSetScore(scoreDTO.getSetThree()))
                .tieBreak(convertContestAttributesSetScore(scoreDTO.getTieBreak()))
                .build();
    }

    private ContestAttributesSetScore convertContestAttributesSetScore(SetScoreDTO set) {
        if (set == null) return null;
        return new ContestAttributesSetScore(set.getParticipantOneScore(), set.getParticipantTwoScore());
    }
}
