package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.TOURNAMENT_NAME_DUPLICATE;

@Component
public class TournamentValidator {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private ValidatorHelper<TournamentDTO> validatorHelper;

    public Set<ErrorDataDTO> validateCreateTournament(TournamentDTO tournamentDTO) {
        Set<ErrorDataDTO> detailedErrorSet = new HashSet<>(validatorHelper.validateObject(tournamentDTO));
        if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent())
            detailedErrorSet.add(new ErrorDataDTO(TOURNAMENT_NAME_DUPLICATE, tournamentDTO.getName()));
        return detailedErrorSet;
    }

}
