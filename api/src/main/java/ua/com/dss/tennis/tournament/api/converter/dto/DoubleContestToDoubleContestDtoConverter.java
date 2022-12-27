package ua.com.dss.tennis.tournament.api.converter.dto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.db.v2.DoubleContest;
import ua.com.dss.tennis.tournament.api.model.dto.DoubleContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TeamDTO;

@Getter
@Setter
public class DoubleContestToDoubleContestDtoConverter extends ContestToContestDtoConverter
        implements Converter<DoubleContest, DoubleContestDTO> {

    @Override
    public DoubleContestDTO convert(MappingContext<DoubleContest, DoubleContestDTO> context) {
        DoubleContest contest = context.getSource();

        return DoubleContestDTO.builder()
                .id(contest.getId())
                .teamOne(TeamDTO.builder()
                        .id(contest.getTeamOne().getId())
                        .playerOne(PlayerDTO.builder().id(contest.getTeamOne().getPlayerOneId()).build())
                        .playerTwo(PlayerDTO.builder().id(contest.getTeamOne().getPlayerTwoId()).build())
                        .build())
                .teamTwo(TeamDTO.builder()
                        .id(contest.getTeamTwo().getId())
                        .playerOne(PlayerDTO.builder().id(contest.getTeamTwo().getPlayerOneId()).build())
                        .playerTwo(PlayerDTO.builder().id(contest.getTeamTwo().getPlayerTwoId()).build())
                        .build())
                .winnerId(contest.getWinnerId())
                .scoreDto(convertSetScoreDto(contest))
                .build();
    }
}