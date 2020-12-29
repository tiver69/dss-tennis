package com.dss.tennis.tournament.bot;

import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.bot.state.BotStateHelper;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Stream;

import static com.dss.tennis.tournament.bot.helper.KeyboardMarkupHelper.*;
import static com.dss.tennis.tournament.bot.helper.MainMenuHelper.START_KEYBOARD;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DssTennisBotFacadeTest {

    private static Integer ID = 1;
    private static BotState CURRENT_BOT_STATE = BotState.MAIN_MENU;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message messageMock;
    @Mock
    private Update updateMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private CallbackQuery callbackQueryMock;
    @Mock
    private BotStateHelper botStateHelperMock;
    @Mock
    private DssTennisUserStateCache userStateCacheMock;

    @InjectMocks
    private DssTennisBotFacade testInstance;

    @ParameterizedTest
    @MethodSource
    public void testBasicCallbackFlow(String buttonQueryData, BotState botState) {
        prepareCallbackFlow();
        when(callbackQueryMock.getData()).thenReturn(buttonQueryData);

        testInstance.handleUpdate(updateMock);

        verify(userStateCacheMock).setUsersCurrentBotState(ID, botState);
        verify(botStateHelperMock).processInputMessage(botState, ID, messageMock);
    }

    @ParameterizedTest
    @MethodSource
    public void testYesNoCallbackFlow(String buttonQueryData, BotState currentBotState, BotState newBotState) {
        prepareCallbackFlow();
        when(callbackQueryMock.getData()).thenReturn(buttonQueryData);
        when(userStateCacheMock.getUsersCurrentBotState(ID)).thenReturn(currentBotState);

        testInstance.handleUpdate(updateMock);

        verify(userStateCacheMock).setUsersCurrentBotState(ID, newBotState);
        verify(botStateHelperMock).processInputMessage(newBotState, ID, messageMock);
    }

    @Test
    public void testDefaultCallbackFlow() {
        prepareCallbackFlow();
        when(callbackQueryMock.getData()).thenReturn("");
        when(userStateCacheMock.getUsersCurrentBotState(ID)).thenReturn(CURRENT_BOT_STATE);

        testInstance.handleUpdate(updateMock);

        verify(userStateCacheMock).setUsersCurrentBotState(ID, CURRENT_BOT_STATE);
        verify(botStateHelperMock).processInputMessage(CURRENT_BOT_STATE, ID, messageMock);
    }

    @Test
    public void testBasicInputMessageFlow() {
        prepareInputMessageFlow();
        when(messageMock.getText()).thenReturn(START_KEYBOARD);

        testInstance.handleUpdate(updateMock);

        verify(userStateCacheMock).setUsersCurrentBotState(ID, BotState.INIT);
        verify(botStateHelperMock).processInputMessage(BotState.INIT, messageMock);
    }

    @Test
    public void testDefaultInputMessageFlow() {
        prepareInputMessageFlow();
        when(messageMock.getText()).thenReturn("");
        when(userStateCacheMock.getUsersCurrentBotState(ID)).thenReturn(CURRENT_BOT_STATE);

        testInstance.handleUpdate(updateMock);

        verify(userStateCacheMock).setUsersCurrentBotState(ID, CURRENT_BOT_STATE);
        verify(botStateHelperMock).processInputMessage(CURRENT_BOT_STATE, messageMock);
    }

    private void prepareCallbackFlow() {
        when(updateMock.hasCallbackQuery()).thenReturn(true);
        when(updateMock.getCallbackQuery()).thenReturn(callbackQueryMock);
        when(callbackQueryMock.getMessage()).thenReturn(messageMock);
        when(callbackQueryMock.getFrom().getId()).thenReturn(ID);
    }

    private void prepareInputMessageFlow() {
        when(updateMock.hasCallbackQuery()).thenReturn(false);
        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.hasText()).thenReturn(true);
        when(messageMock.getFrom().getId()).thenReturn(ID);
    }

    private static Stream<Arguments> testBasicCallbackFlow() {
        return Stream.of(
                Arguments.of(CREATE_TOURNAMENT_CALLBACK_DATA, BotState.ASK_NAME),
                Arguments.of(UPDATE_TOURNAMENT_CALLBACK_DATA, BotState.ADD_MATCH)
        );
    }

    private static Stream<Arguments> testYesNoCallbackFlow() {
        return Stream.of(
                Arguments.of(YES_CALLBACK_DATA, BotState.ASK_ADDITIONAL_PLAYERS, BotState.ASK_PLAYERS),
                Arguments.of(YES_CALLBACK_DATA, BotState.CONFIRM_TOURNAMENT, BotState.SAVE_TOURNAMENT),
                Arguments.of(YES_CALLBACK_DATA, BotState.MAIN_MENU, BotState.MAIN_MENU),
                Arguments.of(NO_CALLBACK_DATA, BotState.ASK_ADDITIONAL_PLAYERS, BotState.CONFIRM_TOURNAMENT),
                Arguments.of(NO_CALLBACK_DATA, BotState.CONFIRM_TOURNAMENT, BotState.MAIN_MENU),
                Arguments.of(NO_CALLBACK_DATA, BotState.MAIN_MENU, BotState.MAIN_MENU)
        );
    }
}
