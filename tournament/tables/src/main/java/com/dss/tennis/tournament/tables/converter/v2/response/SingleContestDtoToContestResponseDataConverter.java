package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestRelationships;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestResponseData;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;

@Getter
@Setter
@AllArgsConstructor
public class SingleContestDtoToContestResponseDataConverter extends ContestDtoToContestResponseDataConverter
        implements Converter<SingleContestDTO, ContestResponseData> {

    private String extraTournamentId;

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