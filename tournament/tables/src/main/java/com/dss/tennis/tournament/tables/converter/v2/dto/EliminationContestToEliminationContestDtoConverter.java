package com.dss.tennis.tournament.tables.converter.v2.dto;

import com.dss.tennis.tournament.tables.model.db.v2.EliminationContest;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@Getter
@Setter
public class EliminationContestToEliminationContestDtoConverter extends ContestToContestDtoConverter
        implements Converter<EliminationContest, EliminationContestDTO> {

    @Override
    public EliminationContestDTO convert(MappingContext<EliminationContest, EliminationContestDTO> context) {
        EliminationContest contest = context.getSource();

        return EliminationContestDTO.builder()
                .id(contest.getId())
                .winnerId(contest.getWinnerId())
                .scoreDto(convertSetScoreDto(contest))
                .build();
    }
}