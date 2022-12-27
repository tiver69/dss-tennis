package ua.com.dss.tennis.tournament.api.converter.dto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.db.v2.EliminationContest;
import ua.com.dss.tennis.tournament.api.model.dto.EliminationContestDTO;

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