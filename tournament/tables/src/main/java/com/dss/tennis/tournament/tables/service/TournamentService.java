package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.error.WarningConstant.PLAYER_DUPLICATION;

@Service
public class TournamentService {

    @Autowired
    private TournamentValidator tournamentValidator;
    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private TournamentHelper tournamentHelper;
    @Autowired
    private WarningHandler warningHandler;

    @Transactional
    public SuccessResponseDTO<TournamentDTO> createNewTournament(TournamentDTO tournamentDTO) {
        List<PlayerDTO> duplicationPlayers = playerHelper.removePlayerDuplicates(tournamentDTO.getPlayers());
        validateCreateTournamentDTO(tournamentDTO);

        Tournament tournament = tournamentHelper.createNewTournamentWithContests(tournamentDTO);
        List<ErrorData> warnings = duplicationPlayers.stream()
                .map(tt -> warningHandler.createWarning(PLAYER_DUPLICATION, tt.getSequenceNumber()))
                .collect(Collectors.toList());

        return new SuccessResponseDTO<>(tournamentHelper.getTournament(tournament.getId()), warnings);
    }

    public TournamentDTO getTournament(Integer tournamentId, RequestParameter requestParameters) {
        return tournamentHelper.getTournament(tournamentId, requestParameters);
    }

    private void validateCreateTournamentDTO(TournamentDTO tournamentDTO) {
        Set<DetailedErrorData> errorSet = new HashSet<>(tournamentValidator
                .validateCreateTournament(tournamentDTO));
        tournamentDTO.getPlayers().stream().map(playerValidator::validatePlayer)
                .forEach(errorSet::addAll);
        playerValidator.validatePlayerQuantity(tournamentDTO.getPlayers()).ifPresent(errorSet::add);
        if (!errorSet.isEmpty()) {
            throw new DetailedException(errorSet);
        }
    }
}
