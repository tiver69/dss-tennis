package com.dss.tennis.tournament.tables.converter.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes.ContestAttributesScore;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes.ContestAttributesSetScore;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestResponseData.ContestResponseDataBuilder;
import com.dss.tennis.tournament.tables.model.definitions.contest.TechDefeat;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.CONTEST;

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
