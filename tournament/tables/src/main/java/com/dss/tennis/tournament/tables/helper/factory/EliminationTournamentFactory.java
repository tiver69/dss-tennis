package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_TYPE_NOT_SUPPORTED;

@Service
public class EliminationTournamentFactory implements AbstractTournamentFactory {

    @Override
    public Tournament createNewContests(Tournament tournament, List<PlayerDTO> players) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }

    @Override
    public TournamentDTO buildExistingTournament(Tournament tournament) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }
}
