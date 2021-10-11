package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
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

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.PLAYER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerValidatorTest {

    private static final String VALID_FIRST_NAME = "FirstName";
    private static final String VALID_LAST_NAME = "LastName  ";

    @Mock
    private PlayerRepository playerRepositoryMock;
    @Mock
    private ValidatorHelper<PlayerDTO> validatorHelperMock;
    @Spy
    private Player playerSpy;

    @InjectMocks
    private PlayerValidator testInstance;

    @Test
    public void shouldPassValidation() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        when(validatorHelperMock.validateObject(playerDTO)).thenReturn(Sets.newSet());
        when(playerRepositoryMock.findByFirstNameAndLastName(VALID_FIRST_NAME, VALID_LAST_NAME))
                .thenReturn(Optional.of(playerSpy));

        Set<DetailedErrorData> result = testInstance.validatePlayer(playerDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnErrorFromHelper() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        DetailedErrorData detailedErrorData = new DetailedErrorData();
        when(validatorHelperMock.validateObject(playerDTO)).thenReturn(Sets.newSet(detailedErrorData));
        when(playerRepositoryMock.findByFirstNameAndLastName(VALID_FIRST_NAME, VALID_LAST_NAME))
                .thenReturn(Optional.of(playerSpy));

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
        when(playerRepositoryMock.findByFirstNameAndLastName(VALID_FIRST_NAME, VALID_LAST_NAME))
                .thenReturn(Optional.empty());

        Set<DetailedErrorData> result = testInstance
                .validatePlayer(new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME));

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream().map(DetailedErrorData::getErrorConstant)
                        .anyMatch(tt -> tt.equals(PLAYER_NOT_FOUND)))
        );
    }
}