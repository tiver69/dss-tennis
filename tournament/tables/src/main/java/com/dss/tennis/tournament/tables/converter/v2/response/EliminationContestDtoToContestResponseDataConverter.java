package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestRelationships;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestResponseData;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;
import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TEAM;

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
