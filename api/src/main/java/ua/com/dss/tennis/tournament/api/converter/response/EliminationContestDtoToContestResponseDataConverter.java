package ua.com.dss.tennis.tournament.api.converter.response;

import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestRelationships;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestResponse.ContestResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.EliminationContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.PLAYER;
import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TEAM;

@AllArgsConstructor
public class EliminationContestDtoToContestResponseDataConverter extends ContestDtoToContestResponseDataConverter
        implements Converter<EliminationContestDTO, ContestResponseData> {

    private final Integer extraTournamentId;

    @Override
    public ContestResponseData convert(MappingContext<EliminationContestDTO, ContestResponseData> context) {
        EliminationContestDTO contestDTO = context.getSource();

        return getCommonContestBuilder(contestDTO, extraTournamentId)
                .relationships(convertContestRelationships(contestDTO))
                .build();
    }

    private ContestRelationships convertContestRelationships(EliminationContestDTO contestDto) {
        String participantResourceType = getParticipantType(contestDto);
        if (participantResourceType == null) return null;

        SimpleResourceObject participantOne = contestDto
                .getParticipantOne() != null ? new SimpleResourceObject(contestDto
                .getParticipantOneId(), participantResourceType) : null;
        SimpleResourceObject participantTwo = contestDto
                .getParticipantTwo() != null ? new SimpleResourceObject(contestDto
                .getParticipantTwoId(), participantResourceType) : null;
        SimpleResourceObject winner = contestDto.getWinnerId() == null ? null : new SimpleResourceObject(contestDto
                .getWinnerId(), participantResourceType);

        return ContestRelationships.builder()
                .participantOne(participantOne)
                .participantTwo(participantTwo)
                .winner(winner)
                .build();
    }

    private String getParticipantType(EliminationContestDTO contestDto) {
        if (contestDto.getParticipantOne() != null)
            return (contestDto.getParticipantOne() instanceof PlayerDTO) ? PLAYER.value : TEAM.value;
        if (contestDto.getParticipantTwo() != null)
            return (contestDto.getParticipantTwo() instanceof PlayerDTO) ? PLAYER.value : TEAM.value;
        return null;
    }
}
