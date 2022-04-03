package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.dto.ContestDTO;

import java.util.List;
import java.util.Set;

public interface AbstractContestFactory {

    boolean createContestsForTournament(Integer tournamentId, Set<Integer> newParticipantsId);

    void removeParticipantFromTournament(Integer participantId, int tournamentId, boolean techDefeat);

    void removeTournamentContests(int tournamentId);

    List<ContestDTO> getContestDTOs(Integer tournamentId);
}
