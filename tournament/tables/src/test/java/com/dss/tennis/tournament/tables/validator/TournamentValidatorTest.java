package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NAME_DUPLICATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentValidatorTest {

    private static final String TOURNAMENT_NAME = "TOURNAMENT_NAME";
    private static final String EMPTY_NAME = "";

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private ValidatorHelper<CreateTournamentDTO> validatorHelper;

    @InjectMocks
    TournamentValidator testInstance;

    @Test
    public void shouldPassValidation() {
        CreateTournamentDTO createTournamentDTO = new CreateTournamentDTO(TOURNAMENT_NAME, TournamentType.ROUND, null);
        when(validatorHelper.validateObject(createTournamentDTO)).thenReturn(Sets.newSet());
        when(tournamentRepository.findByName(TOURNAMENT_NAME)).thenReturn(Optional.empty());

        Set<DetailedErrorData> result = testInstance.validateCreateTournament(createTournamentDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldFailValidationWithEmptyName() {
        CreateTournamentDTO createTournamentDTO = new CreateTournamentDTO(EMPTY_NAME, TournamentType.ROUND, null);
        DetailedErrorData detailedErrorData = new DetailedErrorData();
        when(validatorHelper.validateObject(createTournamentDTO)).thenReturn(Sets.newSet(detailedErrorData));
        when(tournamentRepository.findByName(EMPTY_NAME)).thenReturn(Optional.empty());

        Set<DetailedErrorData> result = testInstance.validateCreateTournament(createTournamentDTO);

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(tt -> tt.equals(detailedErrorData)))
        );
    }

    @Test
    public void shouldFailValidationExistingTournamentName() {
        CreateTournamentDTO createTournamentDTO = new CreateTournamentDTO(TOURNAMENT_NAME, TournamentType.ROUND, null);
        when(validatorHelper.validateObject(createTournamentDTO)).thenReturn(Sets.newSet());
        when(tournamentRepository.findByName(TOURNAMENT_NAME)).thenReturn(Optional.of(new Tournament()));

        Set<DetailedErrorData> result = testInstance.validateCreateTournament(createTournamentDTO);

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(tt -> tt.getErrorConstant().equals(TOURNAMENT_NAME_DUPLICATE)))
        );
    }
}
