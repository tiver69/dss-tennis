package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetEliminationContest;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

@AllArgsConstructor
public class EliminationContestDtoToGetObjectConverter implements Converter<EliminationContestDTO,
        GetEliminationContest> {

    private ModelMapper modelMapper;

    @Override
    public GetEliminationContest convert(MappingContext<EliminationContestDTO, GetEliminationContest> context) {
        EliminationContestDTO sourceContest = context.getSource();
        GetEliminationContest destinationContest = context.getDestination();

        ContestDTO firstParentContest = sourceContest.getFirstParentContestDto();
        ContestDTO secondParentContest = sourceContest.getSecondParentContestDto();
        destinationContest
                .setFirstParentContest(modelMapper.map(firstParentContest, getSourceClass(firstParentContest)));
        destinationContest
                .setSecondParentContest(modelMapper.map(secondParentContest, getSourceClass(secondParentContest)));

        return destinationContest;
    }

    private Class<? extends GetContest> getSourceClass(ContestDTO contest) {
        if (contest instanceof EliminationContestDTO) return GetEliminationContest.class;
        return GetContest.class;
    }
}
