package com.dss.tennis.tournament.tables.converter.dto;

import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@Getter
@Setter
public class SingleContestToSingleContestDtoConverter extends ContestToContestDtoConverter
        implements Converter<SingleContest, SingleContestDTO> {

    @Override
    public SingleContestDTO convert(MappingContext<SingleContest, SingleContestDTO> context) {
        SingleContest contest = context.getSource();

        return SingleContestDTO.builder()
                .id(contest.getId())
                .playerOne(PlayerDTO.builder().id(contest.getPlayerOneId()).build())
                .playerTwo(PlayerDTO.builder().id(contest.getPlayerTwoId()).build())
                .winnerId(contest.getWinnerId())
                .scoreDto(convertSetScoreDto(contest))
                .build();
    }
}