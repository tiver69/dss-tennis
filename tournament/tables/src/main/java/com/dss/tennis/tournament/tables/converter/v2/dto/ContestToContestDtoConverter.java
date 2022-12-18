package com.dss.tennis.tournament.tables.converter.v2.dto;

import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.Score;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;

import java.util.function.Function;
import java.util.stream.Collectors;

public class ContestToContestDtoConverter {

    public ScoreDTO convertSetScoreDto(Contest contest) {
        return ScoreDTO.builder()
                .participantOneScoreId(contest.getParticipantOneScore().getId())
                .participantTwoScoreId(contest.getParticipantTwoScore().getId())
                .sets(contest.getSets() == null ? null : contest.getSets().stream()
                        .collect(Collectors.toMap(SetScore::getSetType, this::convertSetScoreDto)))
                .setOne(convertSetScoreDto(contest, Score::getSetOne))
                .setTwo(convertSetScoreDto(contest, Score::getSetTwo))
                .setThree(convertSetScoreDto(contest, Score::getSetThree))
                .tieBreak(convertSetScoreDto(contest, Score::getTieBreak))
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

    private SetScoreDTO convertSetScoreDto(Contest contest, Function<Score, Byte> scoreFunction) {
        return SetScoreDTO.builder()
                .participantOneScore(scoreFunction.apply(contest.getParticipantOneScore()))
                .participantTwoScore(scoreFunction.apply(contest.getParticipantTwoScore()))
                .build();
    }

    private TechDefeatDTO convertTechDefeatDto(Contest contest) {
        return new TechDefeatDTO(contest.getParticipantOneScore().getTechDefeat(), contest.getParticipantTwoScore()
                .getTechDefeat());
    }
}
