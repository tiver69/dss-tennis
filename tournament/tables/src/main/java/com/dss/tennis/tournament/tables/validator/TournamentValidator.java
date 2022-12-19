package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.model.db.v1.StatusType;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.request.PatchTournament;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.*;

@Component
public class TournamentValidator {

    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private ValidatorHelper<TournamentDTO> validatorHelper;

    public Set<ErrorDataDTO> validateCreateTournament(TournamentDTO tournamentDTO) {
        Set<ErrorDataDTO> errors = new HashSet<>(validatorHelper.validateObject(tournamentDTO));
        Integer existingPlayerId = tournamentRepository.getTournamentIdByName(tournamentDTO.getName());
        if (existingPlayerId != null && tournamentDTO.getId() != existingPlayerId)
            errors.add(new ErrorDataDTO(TOURNAMENT_NAME_DUPLICATE, tournamentDTO.getName()));
        return errors;
    }

    public Set<ErrorDataDTO> validateTournamentPatch(PatchTournament patch, Integer tournamentId) {
        boolean isTournamentWithContests = contestRepository.isTournamentHasContests(tournamentId);
        if (!isTournamentWithContests) return Set.of();

        Set<ErrorDataDTO> errors = new HashSet<>();
        if (patch.getParticipantType().isPresent()) errors.add(new ErrorDataDTO(PARTICIPANT_TYPE_UPDATE_FORBIDDEN));
        if (patch.getTournamentType().isPresent()) errors.add(new ErrorDataDTO(TOURNAMENT_TYPE_UPDATE_FORBIDDEN));
        return errors;
    }

    public Set<ErrorDataDTO> validateUpdatedTournament(TournamentDTO updatedTournament) {
        Set<ErrorDataDTO> errors = validateCreateTournament(updatedTournament);
        boolean isTournamentWithContests = contestRepository.isTournamentHasContests(updatedTournament.getId());
        if (isTournamentWithContests && updatedTournament.getStatus() == StatusType.IN_PROGRESS && updatedTournament
                .getBeginningDate().isAfter(LocalDate.now())) {
            errors.add(new ErrorDataDTO(BEGINNING_DATE_UPDATE_FORBIDDEN));
        }
        return errors;
    }
}
