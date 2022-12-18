package com.dss.tennis.tournament.tables.converter.v2.dto;

import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

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
                .techDefeat(contest.isTechDefeat())
                .scoreDto(convertSetScoreDto(contest))
                .build();
    }
}