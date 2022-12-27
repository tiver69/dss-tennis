package ua.com.dss.tennis.tournament.api.helper.factory;

import ua.com.dss.tennis.tournament.api.model.db.v2.Contest;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;

import java.util.List;
import java.util.function.Consumer;

public interface AbstractContestFactory {

    void createContestsForTournament(Integer tournamentId, List<Integer> newParticipantsId);

    void removeParticipantFromTournament(Integer participantId, int tournamentId, boolean techDefeat);

    void removeTournamentContests(int tournamentId);

    ContestDTO getBasicContestDTO(Integer contestId, Integer tournamentId);

    ContestDTO getContestDTO(Integer contestId, Integer tournamentId);

    Iterable<ContestDTO> getContestDTOs(Integer tournamentId);

    Iterable<ContestDTO> getContestDTOsWithParticipants(Integer tournamentId);

    Class<? extends Contest> getContestParticipantClass();

    Class<? extends ContestDTO> getContestParticipantDtoClass();

    Consumer<Integer> getParticipantEnrollingQuantityValidationRule();
}
