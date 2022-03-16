package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoundContestFactory implements AbstractContestFactory {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private ContestHelper contestHelper;
    @Autowired
    private ConverterHelper converterHelper;

    @Override
    public void createContests(Tournament tournament, List<PlayerDTO> players) {
        List<Player> repositoryPlayers = players.stream().map(player -> playerHelper.getPlayer(player))
                .collect(Collectors.toList());

        for (int i = 0; i < repositoryPlayers.size(); i++) {
            for (int j = i + 1; j < repositoryPlayers.size(); j++) {
                contestHelper.createNewContest(repositoryPlayers.get(i), repositoryPlayers.get(j), tournament);
            }
        }
    }


    @Override
    public void createContestsForNewPlayers(Integer tournamentId, List<Integer> currentPlayers,
                                            Set<Integer> newPlayers) {
        newPlayers.forEach(newPlayerId -> {
            for (Integer currentPlayerId : currentPlayers)
                contestHelper.createNewContest(newPlayerId, currentPlayerId, tournamentId);
            currentPlayers.add(newPlayerId);
        });
    }

    @Override
    public List<ContestDTO> getContestDTOs(Integer tournamentId,
                                           Class<? extends ContestDTO> contestParticipantType) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        return contests.stream()
                .map(contest -> converterHelper.convert(contest, contestParticipantType)).collect(Collectors.toList());
    }
}
