package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;

import java.util.List;

public interface AbstractTournamentFactory {
    Tournament build(Tournament tournament, List<PlayerDTO> players);
}
