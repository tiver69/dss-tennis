package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.TechDefeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ContestValidator {

    @Autowired
    private ValidatorHelper<TechDefeatDTO> techDefeatDtoValidatorHelper;


    public Set<ErrorDataDTO> validateTechDefeat(TechDefeatDTO techDefeatDto) {
        return techDefeatDtoValidatorHelper.validateObject(techDefeatDto);
    }
}
