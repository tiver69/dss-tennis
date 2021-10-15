package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.factory.AbstractTournamentFactory;
import com.dss.tennis.tournament.tables.helper.factory.EliminationTournamentFactory;
import com.dss.tennis.tournament.tables.helper.factory.RoundTournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NOT_FOUND;

@Service
public class TournamentHelper {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private RoundTournamentFactory roundTournamentFactory;
    @Autowired
    private EliminationTournamentFactory eliminationTournamentFactory;

    @Transactional
    public Tournament createNewTournamentWithContests(TournamentDTO tournamentDTO) {
        Tournament tournament = createNewTournament(tournamentDTO);

        AbstractTournamentFactory tournamentFactory = getTournamentFactory(tournamentDTO.getType());
        tournamentFactory.createNewContests(tournament, tournamentDTO.getPlayers());

        return tournament;
    }

    public Tournament createNewTournament(TournamentDTO tournamentDTO) {
        Tournament tournament = Tournament.builder().name(tournamentDTO.getName())
                .type(tournamentDTO.getType())
                .inProgress(true)
                .build();
        return tournamentRepository.save(tournament);
    }

    public TournamentDTO getTournament(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->
                new DetailedException(TOURNAMENT_NOT_FOUND, tournamentId.toString()));

        return getTournamentFactory(tournament.getType()).buildExistingTournament(tournament);
    }

    private AbstractTournamentFactory getTournamentFactory(TournamentType type) {
        return type == TournamentType.ELIMINATION ? eliminationTournamentFactory : roundTournamentFactory;
    }
}
