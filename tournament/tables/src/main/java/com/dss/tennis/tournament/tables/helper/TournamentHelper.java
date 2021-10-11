package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.helper.factory.AbstractTournamentFactory;
import com.dss.tennis.tournament.tables.helper.factory.EliminationTournamentFactory;
import com.dss.tennis.tournament.tables.helper.factory.RoundTournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TournamentHelper {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private RoundTournamentFactory roundTournamentFactory;
    @Autowired
    private EliminationTournamentFactory eliminationTournamentFactory;

    @Transactional
    public Tournament createNewTournamentWithContests(CreateTournamentDTO createTournamentDTO) {
        Tournament tournament = createNewTournament(createTournamentDTO);

        AbstractTournamentFactory tournamentFactory = getTournamentFactory(createTournamentDTO.getType());
        tournamentFactory.build(tournament, createTournamentDTO.getPlayers());

        return tournament;
    }

    public Tournament createNewTournament(CreateTournamentDTO createTournamentDTO) {
        Tournament tournament = Tournament.builder().name(createTournamentDTO.getName())
                .type(createTournamentDTO.getType())
                .inProgress(true)
                .build();
        return tournamentRepository.save(tournament);
    }

    private AbstractTournamentFactory getTournamentFactory(TournamentType type) {
        return type == TournamentType.ELIMINATION ? eliminationTournamentFactory : roundTournamentFactory;
    }
}
