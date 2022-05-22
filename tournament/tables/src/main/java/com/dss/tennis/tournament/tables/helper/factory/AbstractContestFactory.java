package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;

import java.util.List;
import java.util.function.Consumer;

public interface AbstractContestFactory {

    void createContestsForTournament(Integer tournamentId, List<Integer> newParticipantsId);

    void removeParticipantFromTournament(Integer participantId, int tournamentId, boolean techDefeat);

    void removeTournamentContests(int tournamentId);

    ContestDTO getBasicContestDTO(Integer contestId, Integer tournamentId);

    Iterable<ContestDTO> getContestDTOs(Integer tournamentId);

    Class<? extends Contest> getContestParticipantClass();

    Class<? extends ContestDTO> getContestParticipantDtoClass();

    Consumer<Integer> getParticipantEnrollingQuantityValidationRule();
}
