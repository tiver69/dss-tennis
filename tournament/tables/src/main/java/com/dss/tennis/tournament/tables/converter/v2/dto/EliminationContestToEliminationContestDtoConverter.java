package com.dss.tennis.tournament.tables.converter.v2.dto;

import com.dss.tennis.tournament.tables.model.db.v2.EliminationContest;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

@Getter
@Setter
public class EliminationContestToEliminationContestDtoConverter implements Converter<EliminationContest,
        EliminationContestDTO> {

    @Override
    public EliminationContestDTO convert(MappingContext<EliminationContest, EliminationContestDTO> context) {
        EliminationContest contest = context.getSource();

        return EliminationContestDTO.builder()
                .id(contest.getId())
                .winnerId(contest.getWinnerId())
                .techDefeat(contest.isTechDefeat())
                .scoreDto(convertScoreDto(contest))
                .build();
    }

    private ScoreDTO convertScoreDto(EliminationContest contest) {
        return ScoreDTO.builder()
                .sets(contest.getSets() == null ? null : contest.getSets().stream()
                        .collect(Collectors.toMap(SetScore::getSetType, this::convertSetScoreDto)))
                .techDefeat(null) //todo: figure this out
                .build();
    }

    private SetScoreDTO convertSetScoreDto(SetScore set) {
        return SetScoreDTO.builder()
                .id(set.getId())
                .participantOneScore(set.getParticipantOne())
                .participantTwoScore(set.getParticipantTwo())
                .build();
    }
}