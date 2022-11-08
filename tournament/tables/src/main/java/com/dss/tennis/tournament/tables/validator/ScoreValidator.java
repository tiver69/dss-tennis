package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.helper.ScoreHelper;
import com.dss.tennis.tournament.tables.model.dto.ContestScorePatchDTO;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.*;
import static com.dss.tennis.tournament.tables.model.db.v2.SetType.*;

@Component
public class ScoreValidator {

    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private ValidatorHelper<SetScoreDTO> setScoreValidatorHelper;
    @Autowired
    private ValidatorHelper<TechDefeatDTO> techDefeatDtoValidatorHelper;

    public Set<ErrorDataDTO> validateUpdateScorePatch(ContestScorePatchDTO scorePatchDto) {
        Set<ErrorDataDTO> errors = new HashSet<>();

        if (scorePatchDto.getTechDefeat() == null && scorePatchDto.getSets() == null) {
            errors.add(ErrorDataDTO.builder().errorConstant(CONTEST_UPDATE_ATTRIBUTES_EMPTY).build());
            return errors;
        }

        if (scorePatchDto.getTechDefeat() != null) {
            errors = techDefeatDtoValidatorHelper.validateObject(scorePatchDto.getTechDefeat());
        }
        if (scorePatchDto.getSets() != null) {
            Set<ErrorDataDTO> structureErrors = scorePatchDto.getSets().keySet().stream()
                    .map(setType -> validateSetScorePatch(scorePatchDto.getSets().get(setType), setType.value))
                    .flatMap(Collection::stream).collect(Collectors.toSet());
            errors.addAll(structureErrors);
        }
        return errors;
    }

    public Set<ErrorDataDTO> validateUpdateScore(ScoreDTO score) {
        Set<ErrorDataDTO> errors = new HashSet<>();
        //todo define rule for tie break
        if (score.isSetScoreNotDefined(SET_ONE) && score.isSetScoreDefined(SET_TWO))
            errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_EMPTY).pointer(SET_ONE.value).build());
        if (score.isSetScoreNotDefined(SET_TWO) && score.isSetScoreDefined(SET_THREE))
            errors.add(ErrorDataDTO.builder().errorConstant(SET_SCORE_EMPTY).pointer(SET_TWO.value).build());
        return errors;
    }

    private Set<ErrorDataDTO> validateSetScorePatch(SetScoreDTO setScoreDTO, String type) {
        Set<ErrorDataDTO> errorDataDTOs = setScoreValidatorHelper.validateObject(setScoreDTO, type);

        return errorDataDTOs.isEmpty() ? validateSetScoreLimit(setScoreDTO
                .getParticipantOneScore(), setScoreDTO.getParticipantTwoScore(), type) : errorDataDTOs;
    }

    private Set<ErrorDataDTO> validateSetScoreLimit(Byte participantOne, Byte participantTwo, String type) {
        //todo determine rules for tie break score
        if (participantOne != null && participantTwo != null && !scoreHelper
                .isSetScoreValid(participantOne, participantTwo))
            return Set.of(ErrorDataDTO.builder().errorConstant(GAME_LIMIT_EXCEEDED).pointer(type)
                    .detailParameter(String.format("%d:%d", participantOne, participantTwo)).build());
        return new HashSet<>();
    }
}
