package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;

import java.util.List;

public interface AbstractTournamentFactory {
    Tournament createNewContests(Tournament tournament, List<PlayerDTO> players);

    TournamentDTO buildExistingTournament(Tournament tournament);
}
