package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class TournamentService {

    @Autowired
    private TournamentValidator tournamentValidator;
    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private TournamentHelper tournamentHelper;

    @Transactional
    public void createNewTournament(CreateTournamentDTO createTournamentDTO) {
        validateCreateTournamentDTO(createTournamentDTO);
        tournamentHelper.createNewTournamentWithContests(createTournamentDTO);
    }

    private void validateCreateTournamentDTO(CreateTournamentDTO createTournamentDTO) {
        Set<DetailedErrorData> errorSet = new HashSet<>(tournamentValidator
                .validateCreateTournament(createTournamentDTO));
        createTournamentDTO.getPlayers().stream().map(playerValidator::validatePlayer)
                .forEach(errorSet::addAll);
        if (!errorSet.isEmpty()) {
            throw new DetailedException(errorSet);
        }
    }
}
