package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.dto.ContestDTO;

import java.util.List;
import java.util.Set;

public interface AbstractContestFactory {

    void createContestsForNewPlayers(Integer tournamentId, List<Integer> currentPlayers, Set<Integer> newPlayers);

    List<ContestDTO> getContestDTOs(Integer tournamentId,
                                    Class<? extends ContestDTO> contestParticipantType);
}
