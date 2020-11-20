package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.v1.Player;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlayerHelperTest {

    private static final String PLAYER_FIRST_NAME = "FirstNameOne";
    private static final String PLAYER_LAST_NAME = "LastNameOne";

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerHelper testInstance;

    @Test
    public void shouldCreateNewPlayer() {
        testInstance.createNewPlayer(PLAYER_FIRST_NAME, PLAYER_LAST_NAME);

        verify(playerRepository).save(any(Player.class));
    }
}