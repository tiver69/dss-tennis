package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoundSingleContestFactory extends RoundContestFactory {

    @Autowired
    private PlayerHelper playerHelper;

    @Override
    public boolean createContestsForTournament(Integer tournamentId, Set<Integer> newPlayerIds) {
        List<Integer> currentPlayerIds = playerHelper.getTournamentParticipants(tournamentId).stream()
                .map(Player::getId).collect(Collectors.toList());
        for (Integer newPlayerId : newPlayerIds) {
            for (Integer currentPlayerId : currentPlayerIds)
                contestHelper.createNewSingleContest(newPlayerId, currentPlayerId, tournamentId);
            currentPlayerIds.add(newPlayerId);
        }
        return currentPlayerIds.size() != 1;
    }

    @Override
    public void removeParticipantFromTournament(Integer playerId, int tournamentId, boolean techDefeat) {
        if (techDefeat)
            contestHelper.getTournamentPlayerContests(playerId, tournamentId).forEach(contestDTO -> contestHelper
                    .updateSingleContestTechDefeatForPlayerRemoving(playerId, contestDTO));
        else
            removeContests(() -> contestRepository.findByPlayerIdAndSingleTournamentId(playerId, tournamentId));
    }

    @Override
    protected Class<? extends ContestDTO> getContestParticipantClass() {
        return SingleContestDTO.class;
    }
}