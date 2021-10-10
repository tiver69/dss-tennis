package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerValidatorTest {

    private static final String VALID_FIRST_NAME = "FirstName";
    private static final String VALID_LAST_NAME = "LastName  ";

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private ValidatorHelper<PlayerDTO> validatorHelper;

    @InjectMocks
    private PlayerValidator testInstance;

    @Test
    public void shouldPassValidation() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        when(validatorHelper.validateObject(playerDTO)).thenReturn(Sets.newSet());

        Set<DetailedErrorData> result = testInstance.validatePlayerName(playerDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnErrorFromHelper() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        DetailedErrorData detailedErrorData = new DetailedErrorData();
        when(validatorHelper.validateObject(playerDTO)).thenReturn(Sets.newSet(detailedErrorData));

        Set<DetailedErrorData> result = testInstance
                .validatePlayerName(new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME));

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(tt -> tt.equals(detailedErrorData)))
        );
    }
}