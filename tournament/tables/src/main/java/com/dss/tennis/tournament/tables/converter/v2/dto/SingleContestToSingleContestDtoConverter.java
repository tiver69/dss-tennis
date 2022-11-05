package com.dss.tennis.tournament.tables.converter.v2.dto;

import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

@Getter
@Setter
public class SingleContestToSingleContestDtoConverter implements Converter<SingleContest,
        SingleContestDTO> {

    @Override
    public SingleContestDTO convert(MappingContext<SingleContest, SingleContestDTO> context) {
        SingleContest contest = context.getSource();

        return SingleContestDTO.builder()
                .id(contest.getId())
                .playerOne(PlayerDTO.builder().id(contest.getPlayerOneId()).build())
                .playerTwo(PlayerDTO.builder().id(contest.getPlayerTwoId()).build())
                .winnerId(contest.getWinnerId())
                .techDefeat(contest.isTechDefeat())
                .scoreDto(convertScoreDto(contest))
                .build();
    }

    private ScoreDTO convertScoreDto(SingleContest contest) {
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

    private TechDefeatDTO convertTechDefeatDto(SingleContest contest) {
        if (!contest.isTechDefeat()) return new TechDefeatDTO(false, false);
        if (contest.getWinnerId() == null) return new TechDefeatDTO(true, true);
        return new TechDefeatDTO(contest.getWinnerId().equals(contest.getPlayerTwoId()), contest.getWinnerId()
                .equals(contest.getPlayerOneId()));
    }
}