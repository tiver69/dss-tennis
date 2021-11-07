package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.factory.AbstractTournamentFactory;
import com.dss.tennis.tournament.tables.helper.factory.EliminationTournamentFactory;
import com.dss.tennis.tournament.tables.helper.factory.RoundTournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.StatusType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NOT_FOUND;

@Service
public class TournamentHelper {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private RoundTournamentFactory roundTournamentFactory;
    @Autowired
    private EliminationTournamentFactory eliminationTournamentFactory;
    @Autowired
    private ConverterHelper converterHelper;

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
                .status(getStatusBaseOnBeginningDate(tournamentDTO.getBeginningDate()))
                .beginningDate(tournamentDTO.getBeginningDate())
                .build();
        return tournamentRepository.save(tournament);
    }

    public TournamentDTO getTournament(Integer tournamentId, boolean includeContests) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->
                new DetailedException(TOURNAMENT_NOT_FOUND, tournamentId.toString()));

        TournamentDTO tournamentDto = converterHelper.convert(tournament, TournamentDTO.class);
        if (includeContests) {
            getTournamentFactory(tournament.getType()).buildExistingTournament(tournamentDto);
        }
        return tournamentDto;
    }

    public StatusType getStatusBaseOnBeginningDate(LocalDate beginningDate) {
        if (beginningDate == null || beginningDate.isAfter(LocalDate.now()))
            return StatusType.PLANNED;
        return StatusType.IN_PROGRESS;
    }

    private AbstractTournamentFactory getTournamentFactory(TournamentType type) {
        return type == TournamentType.ELIMINATION ? eliminationTournamentFactory : roundTournamentFactory;
    }
}
