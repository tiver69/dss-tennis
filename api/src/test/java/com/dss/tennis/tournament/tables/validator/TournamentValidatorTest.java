package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.TOURNAMENT_NAME_DUPLICATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentValidatorTest {

    private static final String TOURNAMENT_NAME = "TOURNAMENT_NAME";
    private static final String EMPTY_NAME = "";

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private ValidatorHelper<TournamentDTO> validatorHelper;

    @InjectMocks
    TournamentValidator testInstance;

    @Test
    public void shouldPassValidation() {
        TournamentDTO tournamentDTO = new TournamentDTO(TOURNAMENT_NAME, TournamentType.ROUND, null);
        when(validatorHelper.validateObject(tournamentDTO)).thenReturn(Sets.newSet());
        when(tournamentRepository.getTournamentIdByName(TOURNAMENT_NAME)).thenReturn(null);

        Set<ErrorDataDTO> result = testInstance.validateCreateTournament(tournamentDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldFailValidationWithEmptyName() {
        TournamentDTO tournamentDTO = new TournamentDTO(EMPTY_NAME, TournamentType.ROUND, null);
        ErrorDataDTO detailedErrorData = new ErrorDataDTO();
        when(validatorHelper.validateObject(tournamentDTO)).thenReturn(Sets.newSet(detailedErrorData));
        when(tournamentRepository.getTournamentIdByName(EMPTY_NAME)).thenReturn(null);

        Set<ErrorDataDTO> result = testInstance.validateCreateTournament(tournamentDTO);

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(tt -> tt.equals(detailedErrorData)))
        );
    }

    @Test
    public void shouldFailValidationExistingTournamentName() {
        TournamentDTO tournamentDTO = new TournamentDTO(TOURNAMENT_NAME, TournamentType.ROUND, null);
        when(validatorHelper.validateObject(tournamentDTO)).thenReturn(Sets.newSet());
        when(tournamentRepository.getTournamentIdByName(TOURNAMENT_NAME)).thenReturn(1);

        Set<ErrorDataDTO> result = testInstance.validateCreateTournament(tournamentDTO);

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(tt -> tt.getErrorKey().equals(TOURNAMENT_NAME_DUPLICATE)))
        );
    }
}