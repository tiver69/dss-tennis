package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.dto.ContestDTO;

import java.util.List;
import java.util.Set;

public interface AbstractContestFactory {

    boolean createContestsForSingleTournament(Integer tournamentId, Set<Integer> newPlayerIds);

    boolean createContestsForDoubleTournament(Integer tournamentId, Set<Integer> newTeamIds);

    List<ContestDTO> getContestDTOs(Integer tournamentId,
                                    Class<? extends ContestDTO> contestParticipantType);
}
