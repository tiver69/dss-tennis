package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface AbstractContestFactory {

    void createContestsForTournament(Integer tournamentId, List<Integer> newParticipantsId, boolean shouldCreateScore);

    void removeParticipantFromTournament(Integer participantId, int tournamentId, boolean techDefeat);

    void removeTournamentContests(int tournamentId);

    ContestDTO getBasicContestDTO(Integer contestId, Integer tournamentId);

    Iterable<ContestDTO> getContestDTOs(Integer tournamentId);

    Iterable<ContestDTO> getContestDTOs(Integer tournamentId, Map<Integer, PlayerDTO> players);

    Class<? extends Contest> getContestParticipantClass();

    Class<? extends ContestDTO> getContestParticipantDtoClass();

    Consumer<Integer> getParticipantEnrollingQuantityValidationRule();
}
