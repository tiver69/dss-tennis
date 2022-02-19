package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetScore;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class ContestDtoToGetResponseConverter<T extends ContestDTO> implements Converter<T, GetContest> {
    @Override
    public GetContest convert(MappingContext<T, GetContest> mappingContext) {
        T sourceContest = mappingContext.getSource();
        GetScore destinationScore = mappingContext.getDestination().getScore();

        destinationScore
                .setSetOne(createSetScore(sourceContest.getSetOnePlayerOne(), sourceContest.getSetOnePlayerTwo()));
        destinationScore
                .setSetTwo(createSetScore(sourceContest.getSetTwoPlayerOne(), sourceContest.getSetTwoPlayerTwo()));
        destinationScore
                .setSetThree(createSetScore(sourceContest.getSetThreePlayerOne(), sourceContest
                        .getSetThreePlayerTwo()));
        destinationScore
                .setTieBreak(createSetScore(sourceContest.getTieBreakPlayerOne(), sourceContest
                        .getTieBreakPlayerTwo()));

        return mappingContext.getDestination();
    }

    private GetScore.Set createSetScore(Byte playerOneScore, Byte playerTwoScore) {
        if (playerOneScore != null || playerTwoScore != null) {
            return new GetScore.Set(ObjectUtils.defaultIfNull(playerOneScore, (byte) 0), ObjectUtils
                    .defaultIfNull(playerTwoScore, (byte) 0));
        }
        return null;
    }
}
