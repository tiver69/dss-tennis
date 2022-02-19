package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;

import java.util.List;

public interface AbstractParticipantFactory {

    List<PlayerDTO> getTournamentPlayers(Integer tournamentId);
}
