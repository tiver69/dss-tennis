package ua.com.dss.tennis.tournament.api.converter.response;

import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestRelationships;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.DoubleContestDTO;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TEAM;

@AllArgsConstructor
public class DoubleContestDtoToContestResponseDataConverter extends ContestDtoToContestResponseDataConverter
        implements Converter<DoubleContestDTO, ContestResponseData> {

    private final Integer extraTournamentId;

    @Override
    public ContestResponseData convert(MappingContext<DoubleContestDTO, ContestResponseData> context) {
        DoubleContestDTO contestDTO = context.getSource();

        return getCommonContestBuilder(contestDTO, extraTournamentId)
                .relationships(convertContestRelationships(contestDTO))
                .build();
    }

    private ContestRelationships convertContestRelationships(DoubleContestDTO contestDto) {
        SimpleResourceObject winner = contestDto.getWinnerId() == null ? null : new SimpleResourceObject(contestDto
                .getWinnerId(), TEAM.value);
        return ContestRelationships.builder()
                .participantOne(new SimpleResourceObject(contestDto.getParticipantOneId(), TEAM.value))
                .participantTwo(new SimpleResourceObject(contestDto.getParticipantTwoId(), TEAM.value))
                .winner(winner)
                .build();
    }
}