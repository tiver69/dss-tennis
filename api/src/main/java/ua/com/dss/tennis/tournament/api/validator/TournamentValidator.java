package ua.com.dss.tennis.tournament.api.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentPatch;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;
import ua.com.dss.tennis.tournament.api.repository.ContestRepository;
import ua.com.dss.tennis.tournament.api.repository.TournamentRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.*;

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

    public Set<ErrorDataDTO> validateTournamentPatch(TournamentPatch patch, Integer tournamentId) {
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
        if (isTournamentWithContests && updatedTournament.getBeginningDate().isAfter(LocalDate.now())) {
            errors.add(new ErrorDataDTO(BEGINNING_DATE_UPDATE_FORBIDDEN));
        }
        return errors;
    }
}
