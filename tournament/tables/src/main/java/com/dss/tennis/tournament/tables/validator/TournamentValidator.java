package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NAME_DUPLICATE;

@Component
public class TournamentValidator {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private ValidatorHelper<TournamentDTO> validatorHelper;

    public Set<DetailedErrorData> validateCreateTournament(TournamentDTO tournamentDTO) {
        Set<DetailedErrorData> detailedErrorSet = new HashSet<>(validatorHelper.validateObject(tournamentDTO));
        if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent())
            detailedErrorSet.add(new DetailedErrorData(TOURNAMENT_NAME_DUPLICATE, tournamentDTO.getName()));
        return detailedErrorSet;
    }

}
