package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import com.dss.tennis.tournament.tables.validator.error.ErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TournamentValidator {

    @Autowired
    private TournamentRepository tournamentRepository;

    public Optional<String> validateTournamentName(String tournamentName) {
        if (tournamentName == null || tournamentName.trim().isEmpty())
            return Optional.of(ErrorConstants.TOURNAMENT_NAME_EMPTY);
        if (tournamentRepository.findByName(tournamentName).isPresent())
            return Optional.of(ErrorConstants.TOURNAMENT_NAME_DUPLICATE);
        return Optional.empty();
    }

}
