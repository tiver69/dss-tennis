package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.helper.KeyboardMarkupHelper;
import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.v1.TournamentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

import static com.dss.tennis.tournament.bot.command.CreateTournamentCommand.STATE_TO_MESSAGE_MAP;
import static com.dss.tennis.tournament.bot.state.BotState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTournamentCommandTest {
    public static final long CHAT_ID = 1;
    public static final int USER_ID = 11;
    public static final int NUMBER_OF_PLAYERS = 3;
    public static final String MESSAGE_TEXT = "messageText";
    public static final String PLAYERS_MESSAGE_TEXT = "player1\nplayer2\nplayer3";
    public static final String TOURNAMENT_NAME = "tournamentName";
    public static final TournamentType TOURNAMENT_TYPE = TournamentType.ELIMINATION;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message messageMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    CreateTournamentDTO createTournamentDTOMock;

    @Mock
    private DssTennisUserStateCache userStateCacheMock;
    @Mock
    private KeyboardMarkupHelper keyboardMarkupHelperMock;

    @InjectMocks
    private CreateTournamentCommand testInstance;

    @BeforeEach
    void setUp() {
        when(userStateCacheMock.getUserCreateTournamentDTO(USER_ID)).thenReturn(createTournamentDTOMock);
        when(messageMock.getChatId()).thenReturn(CHAT_ID);
    }

    @Test
    public void shouldExecuteCreateTournamentCommandForAskNameState() {
        when(userStateCacheMock.getUsersCurrentBotState(USER_ID)).thenReturn(ASK_NAME);

        SendMessage result = testInstance.execute(USER_ID, messageMock);

        verify(userStateCacheMock).setUsersCurrentBotState(USER_ID, ASK_TYPE);
        checkResultSendMessage(result, ASK_NAME);
    }


    @Test
    public void shouldExecuteCreateTournamentCommandForAskTypeState() {
        when(userStateCacheMock.getUsersCurrentBotState(USER_ID)).thenReturn(ASK_TYPE);
        when(messageMock.getText()).thenReturn(MESSAGE_TEXT);

        SendMessage result = testInstance.execute(USER_ID, messageMock);

        verify(createTournamentDTOMock).setName(MESSAGE_TEXT);
        verify(userStateCacheMock).setUsersCurrentBotState(USER_ID, ASK_PLAYERS);
        verify(keyboardMarkupHelperMock).getTournamentTypeMarkup();
        checkResultSendMessage(result, ASK_TYPE);
    }

    @Test
    public void shouldExecuteCreateTournamentCommandForAskPlayersState() {
        when(userStateCacheMock.getUsersCurrentBotState(USER_ID)).thenReturn(ASK_PLAYERS);

        SendMessage result = testInstance.execute(USER_ID, messageMock);

        verify(userStateCacheMock).setUsersCurrentBotState(USER_ID, ASK_ADDITIONAL_PLAYERS);
        checkResultSendMessage(result, ASK_PLAYERS);
    }

    @Test
    public void shouldExecuteCreateTournamentCommandForAskAdditionalPlayersState() {
        ArrayList<String> players = new ArrayList<>();
        when(userStateCacheMock.getUsersCurrentBotState(USER_ID)).thenReturn(ASK_ADDITIONAL_PLAYERS);
        when(createTournamentDTOMock.getPlayers()).thenReturn(players);
        when(messageMock.getText()).thenReturn(PLAYERS_MESSAGE_TEXT);

        SendMessage result = testInstance.execute(USER_ID, messageMock);

        verify(userStateCacheMock, never()).setUsersCurrentBotState(eq(USER_ID), any(BotState.class));
        verify(keyboardMarkupHelperMock).getYesNoMarkup();
        assertEquals(3, players.size());
        checkResultSendMessage(result, ASK_ADDITIONAL_PLAYERS);
    }

    @Test
    public void shouldExecuteCreateTournamentCommandForConfirmTournamentState() {
        when(userStateCacheMock.getUsersCurrentBotState(USER_ID)).thenReturn(CONFIRM_TOURNAMENT);
        when(createTournamentDTOMock.getPlayers().size()).thenReturn(NUMBER_OF_PLAYERS);
        when(createTournamentDTOMock.getName()).thenReturn(TOURNAMENT_NAME);
        when(createTournamentDTOMock.getType()).thenReturn(TOURNAMENT_TYPE);

        SendMessage result = testInstance.execute(USER_ID, messageMock);

        verify(userStateCacheMock, never()).setUsersCurrentBotState(eq(USER_ID), any(BotState.class));
        verify(keyboardMarkupHelperMock).getYesNoMarkup();
        checkResultSendMessage(result, CONFIRM_TOURNAMENT);
    }

    @Test
    public void shouldExecuteCreateTournamentCommandForSaveTournamentState() {
        when(userStateCacheMock.getUsersCurrentBotState(USER_ID)).thenReturn(SAVE_TOURNAMENT);

        SendMessage result = testInstance.execute(USER_ID, messageMock);

        verify(userStateCacheMock).setUsersCurrentBotState(USER_ID, MAIN_MENU);
        checkResultSendMessage(result, SAVE_TOURNAMENT);
    }

    private void checkResultSendMessage(SendMessage result, BotState state) {
        String expectedText = CONFIRM_TOURNAMENT.equals(state) ?
                String.format(STATE_TO_MESSAGE_MAP.get(state), TOURNAMENT_NAME,
                        TOURNAMENT_TYPE.toString().toLowerCase(), NUMBER_OF_PLAYERS)
                : STATE_TO_MESSAGE_MAP.get(state);
        Assertions.assertAll(
                () -> assertEquals(expectedText, result.getText()),
                () -> assertEquals(Long.toString(CHAT_ID), result.getChatId())
        );
    }
}
