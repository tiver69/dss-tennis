package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.TOURNAMENT_TYPE_NOT_SUPPORTED;

@Service
public class EliminationContestFactory implements AbstractContestFactory {

    @Override
    public boolean createContestsForTournament(Integer tournamentId, Set<Integer> newPlayerIds) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }

    @Override
    public void removeParticipantFromTournament(Integer teamId, int tournamentId, boolean techDefeat) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }

    @Override
    public List<ContestDTO> getContestDTOs(Integer tournamentId) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }
}
