package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoAttributes;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.CONTEST_INFO;

@AllArgsConstructor
public class DoubleContestDtoToContestInfoResponseDataConverter extends ContestDtoToContestInfoResponseDataConverter
        implements Converter<DoubleContestDTO, ContestInfoResponseData> {

    private final Integer extraTournamentId;

    @Override
    public ContestInfoResponseData convert(MappingContext<DoubleContestDTO, ContestInfoResponseData> mappingContext) {
        DoubleContestDTO contest = mappingContext.getSource();

        return ContestInfoResponseData.builder()
                .id(contest.getId())
                .type(CONTEST_INFO.value)
                .attributes(convertContestInfoAttributes(contest))
                .links(Links.builder()
                        .self(String.format(CONTEST_INFO.selfLinkFormat, extraTournamentId, contest.getId()))
                        .build())
                .build();
    }

    private ContestInfoAttributes convertContestInfoAttributes(DoubleContestDTO contestDto) {
        ContestInfoAttributes.ContestInfoAttributesBuilder builder = ContestInfoAttributes.builder()
                .participantOne(convertTeamNameString(contestDto.getTeamOne()))
                .participantTwo(convertTeamNameString(contestDto.getTeamTwo()));
        convertContestScoreInfoAttributes(contestDto.getScoreDto(), builder);

        return builder.build();
    }

    private String convertTeamNameString(TeamDTO team) {
        return String
                .format("%s %s - %s %s", team.getPlayerOne().getFirstName(), team.getPlayerOne().getLastName(), team
                        .getPlayerTwo().getFirstName(), team.getPlayerTwo().getLastName());
    }
}
