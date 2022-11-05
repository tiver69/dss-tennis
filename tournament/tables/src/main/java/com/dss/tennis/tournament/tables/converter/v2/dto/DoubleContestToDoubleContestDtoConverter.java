package com.dss.tennis.tournament.tables.converter.v2.dto;

import com.dss.tennis.tournament.tables.model.db.v2.DoubleContest;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

@Getter
@Setter
public class DoubleContestToDoubleContestDtoConverter implements Converter<DoubleContest,
        DoubleContestDTO> {

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
                .scoreDto(convertScoreDto(contest))
                .build();
    }

    private ScoreDTO convertScoreDto(DoubleContest contest) {
        return ScoreDTO.builder()
                .sets(contest.getSets() == null ? null : contest.getSets().stream()
                        .collect(Collectors.toMap(SetScore::getSetType, this::convertSetScoreDto)))
                .techDefeat(convertTechDefeatDto(contest))
                .build();
    }

    private SetScoreDTO convertSetScoreDto(SetScore set) {
        return SetScoreDTO.builder()
                .id(set.getId())
                .participantOneScore(set.getParticipantOne())
                .participantTwoScore(set.getParticipantTwo())
                .build();
    }

    private TechDefeatDTO convertTechDefeatDto(DoubleContest contest) {
        if (!contest.isTechDefeat()) return new TechDefeatDTO(false, false);
        if (contest.getWinnerId() == null) return new TechDefeatDTO(true, true);
        return new TechDefeatDTO(contest.getWinnerId().equals(contest.getTeamTwo().getId()), contest.getWinnerId()
                .equals(contest.getTeamOne().getId()));
    }
}