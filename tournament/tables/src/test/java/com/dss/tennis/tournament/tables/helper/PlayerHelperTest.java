package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.PLAYER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerHelperTest {

    private static final String PLAYER_FIRST_NAME = "FirstNameOne";
    private static final String PLAYER_LAST_NAME = "LastNameOne";

    @Mock
    private PlayerRepository playerRepositoryMock;
    @Spy
    private Player playerSpy;

    @InjectMocks
    private PlayerHelper testInstance;

    @Test
    public void shouldGetPlayerByPlayerDTO() {
        when(playerRepositoryMock.findByFirstNameAndLastName(PLAYER_FIRST_NAME, PLAYER_LAST_NAME))
                .thenReturn(Optional.of(playerSpy));

        Player result = testInstance.getPlayer(new PlayerDTO(PLAYER_FIRST_NAME, PLAYER_LAST_NAME));

        Assertions.assertEquals(playerSpy, result);
    }

    @Test
    public void shouldThrowErrorWhenPlayerNotFoundByPlayerDTO() {
        when(playerRepositoryMock.findByFirstNameAndLastName(PLAYER_FIRST_NAME, PLAYER_LAST_NAME))
                .thenReturn(Optional.empty());

        DetailedException result = Assertions.assertThrows(DetailedException.class, () -> {
            testInstance.getPlayer(new PlayerDTO(PLAYER_FIRST_NAME, PLAYER_LAST_NAME));
        });
        Assertions.assertAll(
                () -> assertFalse(result.getErrors().isEmpty()),
                () -> assertEquals(1, result.getErrors().size()),
                () -> assertTrue(result.getErrors().stream().map(DetailedErrorData::getErrorConstant)
                        .allMatch(errorConstant -> PLAYER_NOT_FOUND == errorConstant))
        );
    }

    @Test
    public void shouldCreateNewPlayer() {
        testInstance.createNewPlayer(PLAYER_FIRST_NAME, PLAYER_LAST_NAME);

        verify(playerRepositoryMock).save(any(Player.class));
    }
}