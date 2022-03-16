package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.FORBIDDEN_PLAYER_QUANTITY;
import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.PLAYER_NOT_FOUND_TOURNAMENT_CREATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerValidatorTest {

    private static final String VALID_FIRST_NAME = "FirstName";
    private static final String VALID_LAST_NAME = "LastName  ";

    @Mock
    private PlayerHelper playerHelperMock;
    @Mock
    private ValidatorHelper<PlayerDTO> validatorHelperMock;

    @Spy
    private DetailedErrorData detailedErrorDataSpy;

    @InjectMocks
    private PlayerValidator testInstance;

    @Test
    public void shouldPassValidation() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        when(validatorHelperMock.validateObject(playerDTO)).thenReturn(Sets.newSet());
        when(playerHelperMock.isPlayerNotExist(playerDTO))
                .thenReturn(false);

        Set<DetailedErrorData> result = testInstance.validatePlayer(playerDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnErrorFromHelper() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        DetailedErrorData detailedErrorData = new DetailedErrorData();
        when(validatorHelperMock.validateObject(playerDTO)).thenReturn(Sets.newSet(detailedErrorData));

        Set<DetailedErrorData> result = testInstance
                .validatePlayer(new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME));

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(tt -> tt.equals(detailedErrorData)))
        );
    }

    @Test
    public void shouldReturnNotFoundError() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        when(validatorHelperMock.validateObject(playerDTO)).thenReturn(Sets.newSet());
        when(playerHelperMock.isPlayerNotExist(playerDTO)).thenReturn(true);

        Set<DetailedErrorData> result = testInstance.validatePlayer(playerDTO);

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream().map(DetailedErrorData::getErrorConstant)
                        .anyMatch(tt -> tt.equals(PLAYER_NOT_FOUND_TOURNAMENT_CREATION)))
        );
    }

    @Test
    public void shouldNotReturnNotFoundError() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        when(validatorHelperMock.validateObject(playerDTO)).thenReturn(Sets.newSet(detailedErrorDataSpy));

        Set<DetailedErrorData> result = testInstance.validatePlayer(playerDTO);

        Assertions.assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.contains(detailedErrorDataSpy))
        );
    }

    @Test
    public void shouldPassPlayerQuantityValidation() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);

        Optional<DetailedErrorData> result = testInstance.validatePlayerQuantity(Lists.list(playerDTO, playerDTO));

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void shouldNotPassPlayerQuantityValidation() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);

        Optional<DetailedErrorData> result = testInstance.validatePlayerQuantity(Lists.list(playerDTO));

        Assertions.assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(FORBIDDEN_PLAYER_QUANTITY, result.get().getErrorConstant())
        );
    }
}