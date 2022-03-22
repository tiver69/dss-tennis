package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.repository.SetScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class ScoreHelper {

    @Autowired
    private SetScoreRepository scoreRepository;

    public ScoreDTO getContestScoreDto(Integer contestId) {
        List<SetScore> sets = getContestSetScores(contestId);

        Map<SetType, SetScoreDTO> scoreSets = new HashMap<>();
        sets.forEach(set -> {
            scoreSets.put(set.getSetType(), new SetScoreDTO(set.getId(), set.getParticipantOne(), set
                    .getParticipantTwo()));
        });
        return new ScoreDTO(scoreSets);
    }

    public List<SetScore> getContestSetScores(Integer contestId) {
        return scoreRepository.findByContestId(contestId);
    }

    public void createScoreForContest(ScoreDTO scoreDto, Integer contestId) {
        scoreDto.getSets().keySet().forEach(setType -> {
            SetScore score = mapSetScore(scoreDto.getSets().get(setType), setType, contestId);
            scoreRepository.save(score);
        });
    }

    public Function<ContestDTO, Integer> determineWinnerId(Map<SetType, SetScoreDTO> setScores) {
        byte result = 0;
        for (SetType key : setScores.keySet()) {
            SetScoreDTO setScore = setScores.get(key);
            byte participantOneScore = setScore.getParticipantOneScore();
            byte participantTwoScore = setScore.getParticipantTwoScore();
            if (participantOneScore == participantTwoScore) return (ContestDTO cc) -> null;

            if (key == SetType.TIE_BREAK) {
                if (participantOneScore > participantTwoScore) result--;
                else result++;
            } else if (isSetScoreComplete(participantOneScore, participantTwoScore)) {
                if (participantOneScore > participantTwoScore) result--;
                else result++;
            } else return (ContestDTO cc) -> null;
        }
        if (result == 0) return (ContestDTO cc) -> null;
        return result < 0 ? ContestDTO::participantOneId : ContestDTO::participantTwoId;
    }

    public boolean isSetScoreValid(byte gameScore, byte otherGameScore) {
        boolean isSimpleScoreValid = (gameScore >= 0 && gameScore <= 6) && (otherGameScore >= 0 && otherGameScore <= 6);
        boolean isExtraSetScoreValid = (gameScore == 7 && (otherGameScore == 6 || otherGameScore == 5))
                || (otherGameScore == 7 && (gameScore == 6 || gameScore == 5));
        return isSimpleScoreValid || isExtraSetScoreValid;
    }

    private SetScore mapSetScore(SetScoreDTO setScore, SetType setType, Integer contestId) {
        return SetScore.builder()
                .contest(Contest.builder().id(contestId).build())
                .participantOne(setScore.getParticipantOneScore())
                .participantTwo(setScore.getParticipantTwoScore())
                .setType(setType)
                .build();
    }

    private boolean isSetScoreComplete(byte participantOne, byte participantTwo) {
        if ((participantOne == 6 && participantTwo <= 4) || (participantOne == 7 && (participantTwo == 6 || participantTwo == 5)))
            return true;
        return (participantOne <= 4 && participantTwo == 6) || ((participantOne == 6 || participantOne == 5) && participantTwo == 7);
    }
}

