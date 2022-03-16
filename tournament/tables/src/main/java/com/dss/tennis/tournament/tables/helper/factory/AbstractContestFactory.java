package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;

import java.util.List;
import java.util.Set;

public interface AbstractContestFactory {

    void createContests(Tournament tournament, List<PlayerDTO> players);

    void createContestsForNewPlayers(Integer tournamentId, List<Integer> currentPlayers, Set<Integer> newPlayers);

    List<ContestDTO> getContestDTOs(Integer tournamentId,
                                    Class<? extends ContestDTO> contestParticipantType);
}
