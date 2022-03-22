package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.helper.ScoreHelper;
import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.GAME_LIMIT_EXCEEDED;
import static com.dss.tennis.tournament.tables.exception.ErrorConstants.SET_SCORE_EMPTY;
import static com.dss.tennis.tournament.tables.model.db.v2.SetType.SET_ONE;
import static com.dss.tennis.tournament.tables.model.db.v2.SetType.SET_TWO;

@Component
public class ScoreValidator {

    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private ValidatorHelper<SetScoreDTO> validatorHelper;

    public Set<ErrorDataDTO> validateCreateScore(ScoreDTO score) {
        Set<ErrorDataDTO> errors = new HashSet<>();
        Map<SetType, SetScoreDTO> sets = score.getSets();
        sets.keySet().stream().map(key -> validateSet(sets.get(key), key)).filter(Objects::nonNull)
                .forEach(errors::addAll);

        if (sets.get(SET_ONE) == null)
            errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_EMPTY).pointer(SET_ONE.value).build());
        else if (sets.get(SetType.SET_THREE) != null && sets.get(SET_TWO) == null)
            errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_EMPTY).pointer(SET_TWO.value).build());

        return errors;
    }

    private Set<ErrorDataDTO> validateSet(SetScoreDTO set, SetType type) {
        Set<ErrorDataDTO> errors = validatorHelper.validateObject(set, type.value);
        if (!errors.isEmpty()) return errors;

        byte participantOne = set.getParticipantOneScore();
        byte participantTwo = set.getParticipantTwoScore();
        if (!scoreHelper.isSetScoreValid(participantOne, participantTwo))
            return Set.of(ErrorDataDTO.builder().errorConstant(GAME_LIMIT_EXCEEDED).pointer(type.value)
                    .detailParameter(String.format("%d:%d", participantOne, participantTwo)).build());
        return null;
    }
}
