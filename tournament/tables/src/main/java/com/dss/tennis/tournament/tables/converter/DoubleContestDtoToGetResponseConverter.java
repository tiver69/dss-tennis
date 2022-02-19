package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetDoubleParticipant;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class DoubleContestDtoToGetResponseConverter
        extends AbstractContestDtoToGetResponseConverter<DoubleContestDTO, GetDoubleParticipant>
        implements Converter<DoubleContestDTO, GetContest> {

    @Override
    public GetContest convert(MappingContext<DoubleContestDTO, GetContest> mappingContext) {
        DoubleContestDTO sourceContest = mappingContext.getSource();
        GetContest<GetDoubleParticipant> destinationContest = mappingContext.getDestination();

        TeamDTO teamOne = sourceContest.getTeamOne();
        TeamDTO teamTwo = sourceContest.getTeamTwo();
        GetDoubleParticipant participantOne = GetDoubleParticipant.builder()
                .id(teamOne.getId())
                .playerOneId(teamOne.getPlayerOne().getId())
                .playerTwoId(teamOne.getPlayerTwo().getId())
                .build();
        GetDoubleParticipant participantTwo = GetDoubleParticipant.builder()
                .id(teamTwo.getId())
                .playerOneId(teamTwo.getPlayerOne().getId())
                .playerTwoId(teamTwo.getPlayerTwo().getId())
                .build();

        destinationContest.setParticipantOne(participantOne);
        destinationContest.setParticipantTwo(participantTwo);

        super.convertSuper(sourceContest, destinationContest);

        return mappingContext.getDestination();
    }
}
