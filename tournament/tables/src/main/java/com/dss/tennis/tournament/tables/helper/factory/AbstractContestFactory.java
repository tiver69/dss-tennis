package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;

import java.util.List;

public interface AbstractContestFactory {

    void createContests(Tournament tournament, List<PlayerDTO> players);

    List<ContestDTO> getContestDTOs(Integer tournamentId,
                                    Class<? extends ContestDTO> contestParticipantType);
}
