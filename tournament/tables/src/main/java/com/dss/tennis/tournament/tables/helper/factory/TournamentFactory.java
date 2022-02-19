package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentFactory {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private RoundTournamentFactory roundTournamentFactory;
    @Autowired
    private EliminationTournamentFactory eliminationTournamentFactory;

    public TournamentDTO populateTournamentDTO(Tournament tournament) {
        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class);
//        getTournamentFactory(tournament.getType()).buildExistingTournament(tournamentDto);
//        List<PlayerDTO> players = playerRepository.findPlayersByTournamentId(tournamentId)
//                .stream().map(player -> converterHelper.convert(player, PlayerDTO.class))
//                .collect(Collectors.toList());
//        tournamentDto.setPlayers(players);
        return tournamentDto;
    }

    private AbstractTournamentFactory getTournamentFactory(TournamentType type) {
        return type == TournamentType.ELIMINATION ? eliminationTournamentFactory : roundTournamentFactory;
    }
}
