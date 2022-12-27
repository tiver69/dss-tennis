package ua.com.dss.tennis.tournament.api.converter.response;

import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestRelationships;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.SingleContestDTO;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.PLAYER;

@AllArgsConstructor
public class SingleContestDtoToContestResponseDataConverter extends ContestDtoToContestResponseDataConverter
        implements Converter<SingleContestDTO, ContestResponseData> {

    private final Integer extraTournamentId;

    @Override
    public ContestResponseData convert(MappingContext<SingleContestDTO, ContestResponseData> context) {
        SingleContestDTO contestDTO = context.getSource();

        return getCommonContestBuilder(contestDTO, extraTournamentId)
                .relationships(convertContestRelationships(contestDTO))
                .build();
    }

    private ContestRelationships convertContestRelationships(SingleContestDTO contestDto) {
        SimpleResourceObject winner = contestDto.getWinnerId() == null ? null : new SimpleResourceObject(contestDto
                .getWinnerId(), PLAYER.value);
        return ContestRelationships.builder()
                .participantOne(contestDto.getParticipantOneId() == null ? null : new SimpleResourceObject(contestDto
                        .getParticipantOneId(), PLAYER.value))
                .participantTwo(contestDto.getParticipantTwoId() == null ? null : new SimpleResourceObject(contestDto
                        .getParticipantTwoId(), PLAYER.value))
                .winner(winner)
                .build();
    }
}