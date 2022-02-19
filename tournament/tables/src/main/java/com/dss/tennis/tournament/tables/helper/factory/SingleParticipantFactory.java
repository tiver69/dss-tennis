package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SingleParticipantFactory implements AbstractParticipantFactory {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ConverterHelper converterHelper;

    @Override
    public List<PlayerDTO> getTournamentPlayers(Integer tournamentId) {
        List<Player> players = playerRepository.findPlayersBySingleTournamentId(tournamentId);
        return players.stream().map(player -> converterHelper.convert(player, PlayerDTO.class))
                .collect(Collectors.toList());

    }
}
