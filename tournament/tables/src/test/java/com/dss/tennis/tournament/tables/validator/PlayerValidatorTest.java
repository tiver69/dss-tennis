package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
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

@ExtendWith(MockitoExtension.class)
class PlayerValidatorTest {

    private static final String VALID_FIRST_NAME = "FirstName";
    private static final String VALID_LAST_NAME = "LastName  ";
    private static final String EMPTY_NAME = "";
    private static final String TRIM_EMPTY_NAME = "    ";

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    PlayerValidator testInstance;

    @ParameterizedTest
    @MethodSource
    public void shouldReturnPlayerFirstOrLastNameEmpty(PlayerDTO player) {
        Optional<DetailedErrorData> result = testInstance.validatePlayerName(player);

        Assertions.assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(ErrorConstants.PLAYER_FIRST_OR_LAST_NAME_EMPTY, result.get().getErrorConstant()),
                () -> assertNull(result.get().getDetailParameter())
        );
    }

    private static Stream<Arguments> shouldReturnPlayerFirstOrLastNameEmpty() {
        return Stream.of(
                Arguments.of(new PlayerDTO(EMPTY_NAME, VALID_LAST_NAME)),
                Arguments.of(new PlayerDTO(TRIM_EMPTY_NAME, EMPTY_NAME)),
                Arguments.of(new PlayerDTO(VALID_FIRST_NAME, EMPTY_NAME))
//                Arguments.of((Object) null) //todo: handle null player
        );
    }

    @Test
    public void shouldPassValidation() {
        Optional<DetailedErrorData> result = testInstance
                .validatePlayerName(new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME));
        assertFalse(result.isPresent());
    }
}