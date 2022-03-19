package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.helper.participant.ParticipantHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import com.dss.tennis.tournament.tables.validator.PageableValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import com.dss.tennis.tournament.tables.validator.participant.PlayerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private TeamHelper teamHelper;
    @Autowired
    private TournamentHelper tournamentHelper;
    @Autowired
    private WarningHandler warningHandler;

    public TournamentDTO createNewTournament(TournamentDTO tournamentDTO) {
        validateCreateTournamentDTO(tournamentDTO);

        Tournament tournament = tournamentHelper.createNewTournament(tournamentDTO);

        return tournamentHelper.getTournament(tournament.getId(), BASIC);
    }

    @Transactional
    public SuccessResponseDTO<TournamentDTO> addParticipantsToTournament(Integer tournamentId,
                                                                         List<ResourceObjectDTO> newParticipantsDto) {
        TournamentDTO tournamentDto = tournamentHelper.getTournament(tournamentId, BASIC);

        Set<ErrorDataDTO> warnings = new HashSet<>();
        ParticipantHelper<?> participantHelper = getParticipantHelper(tournamentDto.getParticipantType());
        Set<Integer> newParticipantIds = participantHelper
                .getParticipantIdsForEnrolling(tournamentId, newParticipantsDto, warnings);

        tournamentHelper.addParticipantsToTournament(tournamentDto, newParticipantIds);
        return new SuccessResponseDTO<>(tournamentHelper.getTournament(tournamentId), warnings);
    }

    public TournamentDTO getTournament(Integer tournamentId, RequestParameter requestParameters) {
        return tournamentHelper.getTournament(tournamentId, requestParameters);
    }

    public SuccessResponseDTO<PageableDTO<TournamentDTO>> getTournamentPage(int page, byte pageSize) {
        Set<ErrorDataDTO> warnings = new HashSet<>();
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
        Set<ErrorDataDTO> errorSet = new HashSet<>(tournamentValidator
                .validateCreateTournament(tournamentDTO));
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);
    }

    private ParticipantHelper<?> getParticipantHelper(ParticipantType participantType) {
        return participantType == ParticipantType.DOUBLE ? teamHelper : playerHelper;
    }
}
