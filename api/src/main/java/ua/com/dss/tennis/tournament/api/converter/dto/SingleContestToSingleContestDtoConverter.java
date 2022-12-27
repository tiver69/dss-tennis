package ua.com.dss.tennis.tournament.api.converter.dto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.db.v2.SingleContest;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.SingleContestDTO;

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