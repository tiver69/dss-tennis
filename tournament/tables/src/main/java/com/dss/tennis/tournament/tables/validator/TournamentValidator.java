package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NAME_DUPLICATE;
import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NAME_EMPTY;

@Component
public class TournamentValidator {

    @Autowired
    private TournamentRepository tournamentRepository;

    public Optional<DetailedErrorData> validateTournamentName(String tournamentName) {
        if (tournamentName == null || tournamentName.trim().isEmpty())
            return Optional.of(new DetailedErrorData(TOURNAMENT_NAME_EMPTY));
        if (tournamentRepository.findByName(tournamentName).isPresent())
            return Optional.of(new DetailedErrorData(TOURNAMENT_NAME_DUPLICATE, tournamentName));
        return Optional.empty();
    }

}
