package ua.com.dss.tennis.tournament.api.converter.response;

import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.ContestInfoAttributes;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.SingleContestDTO;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.CONTEST_INFO;

@AllArgsConstructor
public class SingleContestDtoToContestInfoResponseDataConverter extends ContestDtoToContestInfoResponseDataConverter
        implements Converter<SingleContestDTO, ContestInfoResponseData> {

    private final Integer extraTournamentId;

    @Override
    public ContestInfoResponseData convert(MappingContext<SingleContestDTO, ContestInfoResponseData> mappingContext) {
        SingleContestDTO contest = mappingContext.getSource();

        return ContestInfoResponseData.builder()
                .id(contest.getId())
                .type(CONTEST_INFO.value)
                .attributes(convertContestInfoAttributes(contest))
                .links(Links.builder()
                        .self(String.format(CONTEST_INFO.selfLinkFormat, extraTournamentId, contest.getId()))
                        .build())
                .build();
    }

    private ContestInfoAttributes convertContestInfoAttributes(SingleContestDTO contestDto) {
        ContestInfoAttributes.ContestInfoAttributesBuilder builder = ContestInfoAttributes.builder()
                .participantOne(convertPlayerNameString(contestDto.getPlayerOne()))
                .participantTwo(convertPlayerNameString(contestDto.getPlayerTwo()));
        convertContestScoreInfoAttributes(contestDto.getScoreDto(), builder);

        return builder.build();
    }

    private String convertPlayerNameString(PlayerDTO player) {
        return String.format("%s %s", player.getFirstName(), player.getLastName());
    }
}
