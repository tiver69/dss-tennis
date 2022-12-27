package ua.com.dss.tennis.tournament.api.converter.response;

import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.ContestInfoAttributes;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.DoubleContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TeamDTO;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.CONTEST_INFO;

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
