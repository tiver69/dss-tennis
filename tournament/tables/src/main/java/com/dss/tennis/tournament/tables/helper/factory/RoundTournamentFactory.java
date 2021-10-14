package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
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
    @Autowired
    private ConverterHelper converterHelper;

    @Override
    public Tournament createNewContests(Tournament tournament, List<PlayerDTO> players) {
        List<Player> repositoryPlayers = players.stream().map(player -> playerHelper.getPlayer(player))
                .collect(Collectors.toList());

        for (int i = 0; i < repositoryPlayers.size(); i++) {
            for (int j = i + 1; j < repositoryPlayers.size(); j++) {
                contestHelper.createNewContest(repositoryPlayers.get(i), repositoryPlayers.get(j), tournament);
            }
        }

        return tournament;
    }

    @Override
    public TournamentDTO buildExistingTournament(Tournament tournament) {
        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class);

        List<ContestDTO> contests = contestHelper.getTournamentContests(tournament.getId()).stream()
                .map(contest -> converterHelper.convert(contest, ContestDTO.class)).collect(Collectors.toList());

        tournamentDto.setContests(contests);
        return tournamentDto;
    }
}
