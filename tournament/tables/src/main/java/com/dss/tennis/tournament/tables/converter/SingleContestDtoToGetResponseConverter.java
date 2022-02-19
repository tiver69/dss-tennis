package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetSingleParticipant;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class SingleContestDtoToGetResponseConverter
        extends AbstractContestDtoToGetResponseConverter<SingleContestDTO, GetSingleParticipant>
        implements Converter<SingleContestDTO, GetContest> {

    @Override
    public GetContest convert(MappingContext<SingleContestDTO, GetContest> mappingContext) {
        SingleContestDTO sourceContest = mappingContext.getSource();
        GetContest<GetSingleParticipant> destinationContest = mappingContext.getDestination();

        GetSingleParticipant participantOne = GetSingleParticipant.builder()
                .id(sourceContest.getPlayerOne().getId()).build();
        GetSingleParticipant participantTwo = GetSingleParticipant.builder()
                .id(sourceContest.getPlayerTwo().getId()).build();

        destinationContest.setParticipantOne(participantOne);
        destinationContest.setParticipantTwo(participantTwo);
//        else {
//            TeamDTO teamOne = ((DoubleContestDTO) sourceContest).getTeamOne();
//            TeamDTO teamTwo = ((DoubleContestDTO) sourceContest).getTeamTwo();
//            GetDoubleParticipant participantOne = GetDoubleParticipant.builder()
//                    .id(teamOne.getId())
//                    .playerOneId(teamOne.getPlayerOne().getId())
//                    .playerTwoId(teamOne.getPlayerTwo().getId())
//                    .build();
//            GetDoubleParticipant participantTwo = GetDoubleParticipant.builder()
//                    .id(teamTwo.getId())
//                    .playerOneId(teamTwo.getPlayerOne().getId())
//                    .playerTwoId(teamTwo.getPlayerTwo().getId())
//                    .build();
//
//            destinationContest.setParticipantOne(participantOne);
//            destinationContest.setParticipantTwo(participantTwo);
//        }

        super.convertSuper(sourceContest, destinationContest);

        return mappingContext.getDestination();
    }
}
