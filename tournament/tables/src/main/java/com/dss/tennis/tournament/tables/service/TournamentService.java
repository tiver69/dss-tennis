package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import com.dss.tennis.tournament.tables.validator.PageableValidator;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.error.WarningConstant.PLAYER_DUPLICATION;
import static com.dss.tennis.tournament.tables.model.dto.RequestParameter.BASIC;

@Service
public class TournamentService {

    @Autowired
    private TournamentValidator tournamentValidator;
    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private PageableValidator pageableValidator;
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
        Set<ErrorData> warnings = duplicationPlayers.stream()
                .map(tt -> warningHandler.createWarning(PLAYER_DUPLICATION, tt.getSequenceNumber()))
                .collect(Collectors.toSet());

        return new SuccessResponseDTO<>(tournamentHelper.getTournament(tournament.getId()), warnings);
    }

    @Transactional
    public SuccessResponseDTO<TournamentDTO> addPlayersToTournament(Integer tournamentId,
                                                                    List<ResourceObjectDTO> participantsDto) {
        TournamentDTO tournamentDto = tournamentHelper.getTournament(tournamentId, BASIC);
        List<Integer> currentPlayerIds = playerHelper.getTournamentPlayerIds(tournamentId);

        Set<ErrorData> warnings = new HashSet<>();
        Set<Integer> newPlayerIds = playerHelper
                .getPlayerIdsForEnrolling(currentPlayerIds, participantsDto, tournamentDto
                        .getParticipantType(), warnings);
        playerValidator.validatePlayerQuantity(currentPlayerIds.size() + newPlayerIds.size());

        tournamentHelper.addPlayersToTournament(tournamentDto, currentPlayerIds, newPlayerIds);
        return new SuccessResponseDTO<>(tournamentHelper.getTournament(tournamentId), warnings);
    }

    public TournamentDTO getTournament(Integer tournamentId, RequestParameter requestParameters) {
        return tournamentHelper.getTournament(tournamentId, requestParameters);
    }

    public SuccessResponseDTO<PageableDTO<TournamentDTO>> getTournamentPage(int page, byte pageSize) {
        Set<ErrorData> warnings = new HashSet<>();
        Pageable pageableRequestParameter = pageableValidator
                .validatePageableRequest(page, pageSize, warnings, ResourceObjectType.TOURNAMENT);

        PageableDTO<TournamentDTO> tournamentsPage = tournamentHelper.getTournamentsPage(pageableRequestParameter);
        Pageable newPageableRequestParameter = pageableValidator
                .validateUpperPage(pageableRequestParameter, tournamentsPage
                        .getTotalPages(), warnings, ResourceObjectType.TOURNAMENT);
        if (newPageableRequestParameter != null) {
            tournamentsPage = tournamentHelper.getTournamentsPage(newPageableRequestParameter);
        }

        return new SuccessResponseDTO<>(tournamentsPage, warnings);
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
