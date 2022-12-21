package com.dss.tennis.tournament.bot.state.cache;


import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DssTennisUserStateCacheTest {

    private static final Integer USER_ONE_ID = 1;
    private static final Integer USER_TWO_ID = 2;
    private static final Integer USER_THREE_ID = 3;
    private static final BotState USER_ONE_STATE = BotState.MAIN_MENU;
    private static final BotState USER_TWO_STATE = BotState.ASK_PLAYERS;
    private static final CreateTournamentDTO USER_ONE_DTO = new CreateTournamentDTO();
    private static final CreateTournamentDTO USER_TWO_DTO = new CreateTournamentDTO();

    private DssTennisUserStateCache testInstance;

    @BeforeEach
    public void setUp() {
        testInstance = new DssTennisUserStateCache();
        testInstance.setUsersCurrentBotState(USER_ONE_ID, USER_ONE_STATE);
        testInstance.setUsersCurrentBotState(USER_TWO_ID, USER_TWO_STATE);
        testInstance.saveUserCreateTournamentDTO(USER_ONE_ID, USER_ONE_DTO);
        testInstance.saveUserCreateTournamentDTO(USER_TWO_ID, USER_TWO_DTO);
    }

    @Test
    public void shouldBeSetUp() {
        assertEquals(USER_ONE_STATE, testInstance.getUsersCurrentBotState(USER_ONE_ID));
        assertEquals(USER_TWO_STATE, testInstance.getUsersCurrentBotState(USER_TWO_ID));
        assertEquals(DssTennisUserStateCache.DEFAULT_STATE,
                testInstance.getUsersCurrentBotState(USER_THREE_ID));
        Assertions.assertAll(
                () -> assertEquals(USER_ONE_STATE, testInstance.getUsersCurrentBotState(USER_ONE_ID)),
                () -> assertEquals(USER_TWO_STATE, testInstance.getUsersCurrentBotState(USER_TWO_ID)),
                () -> assertEquals(DssTennisUserStateCache.DEFAULT_STATE, testInstance.getUsersCurrentBotState(USER_THREE_ID)),
                () -> assertEquals(USER_ONE_DTO, testInstance.getUserCreateTournamentDTO(USER_ONE_ID)),
                () -> assertEquals(USER_TWO_DTO, testInstance.getUserCreateTournamentDTO(USER_TWO_ID)),
                () -> assertNotNull(testInstance.getUserCreateTournamentDTO(USER_THREE_ID))
        );
    }

    @Test
    public void shouldClearForUser() {
        testInstance.clearForUser(USER_ONE_ID);

        Assertions.assertNotSame(USER_ONE_DTO, testInstance.getUserCreateTournamentDTO(USER_ONE_ID));
    }
}
