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

        GetSingleParticipant participantOne = new GetSingleParticipant(sourceContest.getPlayerOne().getId());
        GetSingleParticipant participantTwo = new GetSingleParticipant(sourceContest.getPlayerTwo().getId());

        destinationContest.setParticipantOne(participantOne);
        destinationContest.setParticipantTwo(participantTwo);
        super.convertSuper(sourceContest, destinationContest);

        return destinationContest;
    }
}
