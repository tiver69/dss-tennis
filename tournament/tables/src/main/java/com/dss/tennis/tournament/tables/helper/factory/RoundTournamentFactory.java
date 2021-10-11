package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundTournamentFactory implements AbstractTournamentFactory {

    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private ContestHelper contestHelper;

    @Override
    public Tournament build(Tournament tournament, List<PlayerDTO> players) {

        List<Player> repositoryPlayers = players.stream().map(player -> playerHelper.getPlayer(player))
                .collect(Collectors.toList());

        for (int i = 0; i < repositoryPlayers.size(); i++) {
            for (int j = i + 1; j < repositoryPlayers.size(); j++) {
                contestHelper.createNewContest(repositoryPlayers.get(i), repositoryPlayers.get(j), tournament);
            }
        }

        return tournament;
    }
}
