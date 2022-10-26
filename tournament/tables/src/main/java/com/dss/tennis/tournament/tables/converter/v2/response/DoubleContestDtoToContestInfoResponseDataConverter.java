package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoAttributes;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.TechDefeat;
import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Map;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.CONTEST;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.CONTEST_INFO;

public class DoubleContestDtoToContestInfoResponseDataConverter implements Converter<DoubleContestDTO,
        ContestInfoResponseData> {

    @Override
    public ContestInfoResponseData convert(MappingContext<DoubleContestDTO, ContestInfoResponseData> mappingContext) {
        DoubleContestDTO contest = mappingContext.getSource();

        return ContestInfoResponseData.builder()
                .id(contest.getId())
                .type(CONTEST_INFO.value)
                .attributes(convertContestInfoAttributes(contest))
                .links(Links.builder()
                        .self(String.format(CONTEST.selfLinkFormat, 1, contest.getId())) //todo tournamentId not here
                        .build())
                .build();
    }


    private ContestInfoAttributes convertContestInfoAttributes(DoubleContestDTO contestDto) {
        ContestInfoAttributes.ContestInfoAttributesBuilder builder = ContestInfoAttributes.builder()
                .participantOne(convertPlayerNameString(contestDto.getTeamOne()))
                .participantTwo(convertPlayerNameString(contestDto.getTeamTwo()));

        if (contestDto.getScoreDto() != null) {
            Map<SetType, SetScoreDTO> sets = contestDto.getScoreDto().getSets();
            String setOneScore = convertSetScoreString(sets.get(SetType.SET_ONE));
            String setTwoScore = convertSetScoreString(sets.get(SetType.SET_TWO));
            String setThreeScore = convertSetScoreString(sets.get(SetType.SET_THREE));

            builder
                    .mainScore(setOneScore + " " + setTwoScore + " " + setThreeScore)
                    .tieBreak(sets.get(SetType.TIE_BREAK).getParticipantOneScore() + ":" + sets.get(SetType.TIE_BREAK)
                            .getParticipantTwoScore())
                    .techDefeat(new TechDefeat());
        }

        return builder.build();
    }

    private String convertPlayerNameString(TeamDTO team) {
        return String
                .format("%s %s - %s %s", team.getPlayerOne().getFirstName(), team.getPlayerOne().getLastName(), team
                        .getPlayerTwo().getFirstName(), team.getPlayerTwo().getLastName());
    }

    private String convertSetScoreString(SetScoreDTO setScore) {
        return String.format("%d:%d", setScore.getParticipantOneScore(), setScore.getParticipantTwoScore());
    }
}
