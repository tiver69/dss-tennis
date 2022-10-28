package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PatchApplierHelper;
import com.dss.tennis.tournament.tables.helper.ScoreHelper;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.helper.participant.ParticipantHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v2.SetScore;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.request.PatchTournament;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
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

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.CONTEST_SCORE_EXISTS;
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
    private WarningHandler warningHandler;
    @Autowired
    private PatchApplierHelper patchApplierHelper;
    @Autowired
    private TeamValidator teamValidator;

    public TournamentDTO createNewTournament(TournamentDTO tournamentDTO) {
        validateCreateTournamentDTO(tournamentDTO);

        Tournament tournament = tournamentHelper.saveTournament(tournamentDTO);

        return tournamentHelper.getTournamentDto(tournament.getId(), BASIC);
    }

    public TournamentDTO updateTournament(PatchTournament patch, Integer tournamentId) {
        TournamentDTO tournament = tournamentHelper.getTournamentDto(tournamentId, BASIC);
        Set<ErrorDataDTO> errorSet = tournamentValidator.validateTournamentPatch(patch, tournamentId);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        TournamentDTO updatedTournament = patchApplierHelper.applyPatch(patch, tournament);
        errorSet = tournamentValidator.validateUpdatedTournament(updatedTournament);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        tournamentHelper.saveTournament(updatedTournament);
        return tournamentHelper.getTournamentDto(tournamentId, BASIC);
    }

    @Transactional
    public SuccessResponseDTO<TournamentDTO> addParticipantsToTournament(Integer tournamentId,
                                                                         List<ResourceObjectDTO> newParticipantsDto) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId, BASIC);

        Set<ErrorDataDTO> warnings = new HashSet<>();
        ParticipantHelper<?, ?> participantHelper = getParticipantHelper(tournamentDto.getParticipantType());
        List<Integer> newParticipantIds = participantHelper
                .getParticipantIdsForEnrolling(tournamentId, newParticipantsDto, warnings);
        getParticipantValidator(tournamentDto.getParticipantType())
                .validateTournamentParticipantQuantity(tournamentDto, newParticipantIds.size());

        tournamentHelper.addParticipantsToTournament(tournamentDto, newParticipantIds);
        return new SuccessResponseDTO<>(tournamentHelper.getTournamentDto(tournamentId), warnings);
    }

    @Transactional
    public SuccessResponseDTO<TournamentDTO> addParticipantsToTournamentWithScore(Integer tournamentId,
                                                                                  List<ResourceObjectDTO> newParticipantsDto) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId, BASIC);

        Set<ErrorDataDTO> warnings = new HashSet<>();
        ParticipantHelper<?, ?> participantHelper = getParticipantHelper(tournamentDto.getParticipantType());
        List<Integer> newParticipantIds = participantHelper
                .getParticipantIdsForEnrolling(tournamentId, newParticipantsDto, warnings);
        getParticipantValidator(tournamentDto.getParticipantType())
                .validateTournamentParticipantQuantity(tournamentDto, newParticipantIds.size());

        tournamentHelper.addParticipantsToTournament(tournamentDto, newParticipantIds, true);
        return new SuccessResponseDTO<>(tournamentHelper.getTournamentDto(tournamentId), warnings);
    }

    public ContestDTO getTournamentContest(Integer contestId, Integer tournamentId) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId, BASIC);
        return contestHelper.getTournamentContestDTO(contestId, tournamentDto, true);
    }

    @Transactional
    public ContestDTO createContestScore(Integer contestId, Integer tournamentId, ScoreDTO scoreDto) {
        if (!scoreHelper.getContestSetScores(contestId).isEmpty()) throw new DetailedException(CONTEST_SCORE_EXISTS);
        Set<ErrorDataDTO> errorSet = scoreValidator.validateCreateScore(scoreDto);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        TournamentDTO tournamentDTO = tournamentHelper.getTournamentDto(tournamentId, BASIC);
        ContestDTO contest = contestHelper.getTournamentContestDTO(contestId, tournamentDTO, false);
        contestHelper.createContestScore(scoreDto, contest);

        return contestHelper.getTournamentContestDTO(contestId, tournamentDTO, true);
    }

    @Transactional
    public ContestDTO updateContestScoreV1(Integer contestId, Integer tournamentId, ScorePatchDTO scorePatchDto) {
        TournamentDTO tournamentDTO = tournamentHelper.getTournamentDto(tournamentId, BASIC);
        List<SetScore> sets = scoreHelper.getContestSetScores(contestId);
        contestValidator.validateContestUpdateV1(contestId, tournamentDTO, sets.isEmpty());
        ScoreDTO scoreDTO = scoreHelper.mapSetScoreToDto(sets);
        Set<ErrorDataDTO> errorSet = scoreValidator.validateUpdateScorePatch(scoreDTO, scorePatchDto);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        ScorePatchDTO patchedScoreDTO = scoreHelper.applyPatch(scoreDTO, scorePatchDto);

        errorSet = scoreValidator.validateUpdateScore(patchedScoreDTO);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        ContestDTO contest = contestHelper.getTournamentContestDTO(contestId, tournamentDTO, false);
        contestHelper.updateContestScore(patchedScoreDTO, contest);

        return contestHelper.getTournamentContestDTO(contestId, tournamentDTO, true);
    }

    @Transactional
    public ContestDTO updateContestScore(Integer contestId, Integer tournamentId, ContestScorePatchDTO scorePatchDto) {
        Set<ErrorDataDTO> errorSet = scoreValidator.validateUpdateScorePatch(scorePatchDto);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        TournamentDTO tournamentDTO = tournamentHelper.getTournamentDto(tournamentId, BASIC);
        List<SetScore> currentScore = scoreHelper.getContestSetScores(contestId);
        contestValidator.validateContestUpdate(contestId, tournamentDTO);
        ContestDTO contest = contestHelper.getTournamentContestDTO(contestId, tournamentDTO, false);
        ScoreDTO currentScoreDTO = scoreHelper.mapSetScoreToDtoWithTechDefeatDetails(currentScore, contest);

        ScoreDTO patchedScoreDTO = scoreHelper.applyPatch(currentScoreDTO, scorePatchDto);
        errorSet = scoreValidator.validateUpdateScore(patchedScoreDTO);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        contestHelper.updateContestScore(patchedScoreDTO, contest);

        return contestHelper.getTournamentContestDTO(contestId, tournamentDTO, true);
    }

    @Transactional
    public ContestDTO updateContestTechDefeat(Integer contestId, Integer tournamentId, TechDefeatDTO techDefeatDto) {
        TournamentDTO tournamentDTO = tournamentHelper.getTournamentDto(tournamentId, BASIC);
        Set<ErrorDataDTO> errorSet = contestValidator.validateTechDefeat(techDefeatDto, contestId, tournamentDTO);
        if (!errorSet.isEmpty()) throw new DetailedException(errorSet);

        ContestDTO contest = contestHelper.getTournamentContestDTO(contestId, tournamentDTO, false);
        contestHelper.updateContestTechDefeat(techDefeatDto, contest);

        return contestHelper.getTournamentContestDTO(contestId, tournamentDTO, true);
    }

    public TournamentDTO getTournament(Integer tournamentId, RequestParameter requestParameters) {
        return tournamentHelper.getTournamentDto(tournamentId, requestParameters);
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

    public TournamentDTO removeParticipantFromTournament(Integer participantId, Integer tournamentId,
                                                         boolean techDefeat) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId, BASIC);
        ParticipantValidator<?> participantValidator = getParticipantValidator(tournamentDto.getParticipantType());
        participantValidator.validateParticipantForRemoving(participantId, tournamentId);

        tournamentHelper.removeParticipantFromTournament(participantId, tournamentDto, techDefeat);
        return tournamentHelper.getTournamentDto(tournamentId);
    }

    public void removeTournament(Integer tournamentId) {
        TournamentDTO tournamentDto = tournamentHelper.getTournamentDto(tournamentId, BASIC);
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
