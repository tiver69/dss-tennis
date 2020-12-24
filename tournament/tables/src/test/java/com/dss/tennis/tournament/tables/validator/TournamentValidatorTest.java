package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.model.v1.Tournament;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import com.dss.tennis.tournament.tables.validator.error.ErrorConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentValidatorTest {

    private static String TOURNAMENT_NAME = "TOURNAMENT_NAME";
    private static String EMPTY_NAME = "";
    private static String TRIM_EMPTY_NAME = "    ";

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    TournamentValidator testInstance;

    @ParameterizedTest
    @MethodSource
    public void shouldReturnTournamentNameEmptyErrorCode(String tournamentName) {
        Optional<String> result = testInstance.validateTournamentName(tournamentName);
        Assertions.assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(ErrorConstants.TOURNAMENT_NAME_EMPTY, result.get())
        );
    }

    private static Stream<Arguments> shouldReturnTournamentNameEmptyErrorCode() {
        return Stream.of(
                Arguments.of(EMPTY_NAME),
                Arguments.of(TRIM_EMPTY_NAME),
                Arguments.of((Object) null)
        );
    }

    @Test
    public void shouldReturnTournamentNameDuplicateErrorCode() {
        when(tournamentRepository.findByName(TOURNAMENT_NAME)).thenReturn(Optional.of(new Tournament()));

        Optional<String> result = testInstance.validateTournamentName(TOURNAMENT_NAME);
        Assertions.assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(ErrorConstants.TOURNAMENT_NAME_DUPLICATE, result.get())
        );
    }

    @Test
    public void shouldPassValidation() {
        Optional<String> result = testInstance.validateTournamentName(TOURNAMENT_NAME);
        assertFalse(result.isPresent());
    }
}