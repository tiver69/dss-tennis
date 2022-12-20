package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PatchApplierHelper;
import com.dss.tennis.tournament.tables.helper.ScoreHelper;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.helper.participant.ParticipantHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentPatch;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.validator.ContestValidator;
import com.dss.tennis.tournament.tables.validator.PageableValidator;
import com.dss.tennis.tournament.tables.validator.ScoreValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import com.dss.tennis.tournament.tables.validator.participant.ParticipantValidator;
import com.dss.tennis.tournament.tables.validator.participant.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.participant.TeamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TOURNAMENT;

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
    private ContestHelper contestHelper;
    @Autowired
    private ContestValidator contestValidator;
    @Autowired
    private ScoreHelper scoreHelper;
    @Autowired
    private ScoreValidator scoreValidator;
    @Autowired
    private TeamHelper teamHelper;
    @Autowired
    private TournamentHelper tournamentHelper;
    @Autowired
    private PatchApplierHelper patchApplierHelper;
    @Autowired
    private TeamValidator teamValidator;

    public TournamentDTO createNewTournament(TournamentDTO tournamentDTO) {
        validateCreateTournamentDTO(tournamentDTO);

        Tournament tournament = tournamentHelper.saveTournament(tournamentDTO);

        return tournamentHelper.getTournamentDto(tournament.getId());
    }

    public TournamentDTO updateTournament(TournamentPatch patch, Integer tournamentId) {
        TournamentDTO tournament = tournamentHelper.getTournamentDto(tournamentId);
        Set<ErrorDataDTO> errorSet = tournamentValidator.validateTournamentPatch(patch, tournamentId);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        TournamentDTO updatedTournament = patchApplierHelper.applyPatch(patch, tournament);
        errorSet = tournamentValidator.validateUpdatedTournament(updatedTournament);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        tournamentHelper.saveTournament(updatedTournament);
        return tournamentHelper.getTournamentDto(tournamentId);
    }

    @Transactional
    public ResponseWarningDTO<TournamentDTO> addParticipantsToTournament(Integer tournamentId,
                                                                         List<ResourceObjectDTO> newParticipantsDto) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId);

        Set<ErrorDataDTO> warnings = new HashSet<>();
        ParticipantHelper<?, ?> participantHelper = getParticipantHelper(tournamentDto.getParticipantType());
        List<Integer> newParticipantIds = participantHelper
                .getParticipantIdsForEnrolling(tournamentId, newParticipantsDto, warnings);
        getParticipantValidator(tournamentDto.getParticipantType())
                .validateTournamentParticipantQuantity(tournamentDto, newParticipantIds.size());

        tournamentHelper.addParticipantsToTournament(tournamentDto, newParticipantIds);
        return new ResponseWarningDTO<>(tournamentHelper.getTournamentDTOWithContests(tournamentId), warnings);
    }

    public ContestDTO getTournamentContest(Integer contestId, Integer tournamentId) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId);
        return contestHelper.getTournamentContestDTO(contestId, tournamentDto);
    }

    @Transactional
    public ContestDTO updateContestScore(Integer contestId, Integer tournamentId, ScoreDTO scorePatchDto) {
        Set<ErrorDataDTO> errorSet = scoreValidator.validateUpdateScorePatch(scorePatchDto);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        TournamentDTO tournamentDTO = tournamentHelper.getTournamentDto(tournamentId);
        ContestDTO contest = contestHelper.getTournamentContestDTO(contestId, tournamentDTO);
        contestValidator.validateContestUpdate(scorePatchDto, contest, tournamentDTO);

        ScoreDTO patchedScoreDTO = scoreHelper.applyPatch(contest.getScoreDto(), scorePatchDto);
        errorSet = scoreValidator.validateUpdateScore(patchedScoreDTO);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        contestHelper.updateContestScore(patchedScoreDTO, contest);

        return contestHelper.getTournamentContestDTO(contestId, tournamentDTO);
    }

    public TournamentDTO getTournament(Integer tournamentId) {
        return tournamentHelper.getTournamentDTOWithContests(tournamentId);
    }

    public ResponseWarningDTO<PageableDTO> getTournamentPage(int page, byte pageSize) {
        Set<ErrorDataDTO> warnings = new HashSet<>();
        Pageable pageableRequestParameter = pageableValidator
                .validatePageableRequest(page, pageSize, warnings, TOURNAMENT);

        PageableDTO<TournamentDTO> tournamentsPage = tournamentHelper.getTournamentsPage(pageableRequestParameter);
        Pageable newPageableRequestParameter = pageableValidator
                .validateUpperPage(pageableRequestParameter, tournamentsPage
                        .getTotalPages(), warnings, TOURNAMENT);
        if (newPageableRequestParameter != null) {
            tournamentsPage = tournamentHelper.getTournamentsPage(newPageableRequestParameter);
        }

        return new ResponseWarningDTO<>(tournamentsPage, warnings);
    }

    public TournamentDTO removeParticipantFromTournament(Integer participantId, Integer tournamentId,
                                                         boolean techDefeat) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId);
        ParticipantValidator<?> participantValidator = getParticipantValidator(tournamentDto.getParticipantType());
        participantValidator.validateParticipantForRemoving(participantId, tournamentId);

        tournamentHelper.removeParticipantFromTournament(participantId, tournamentDto, techDefeat);
        return tournamentHelper.getTournamentDTOWithContests(tournamentId);
    }

    public void removeTournament(Integer tournamentId) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId);
        tournamentHelper.removeTournament(tournamentDto);
    }

    private void validateCreateTournamentDTO(TournamentDTO tournamentDTO) {
        Set<ErrorDataDTO> errorSet = new HashSet<>(tournamentValidator
                .validateCreateTournament(tournamentDTO));
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);
    }

    private ParticipantHelper<?, ?> getParticipantHelper(ParticipantType participantType) {
        return participantType == ParticipantType.DOUBLE ? teamHelper : playerHelper;
    }

    private ParticipantValidator<?> getParticipantValidator(ParticipantType participantType) {
        return participantType == ParticipantType.DOUBLE ? teamValidator : playerValidator;
    }
}