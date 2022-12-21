package com.dss.tennis.tournament.bot.state;

import com.dss.tennis.tournament.bot.command.BotCommandFactory;
import com.dss.tennis.tournament.bot.command.DssBotCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BotStateHelperTest {

    private static final BotState BOT_STATE = BotState.ASK_TYPE;
    private static final Integer USER_ID = 1;

    @Mock
    private BotCommandFactory commandFactoryMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message messageMock;
    @Mock
    private DssBotCommand botCommandMock;

    @InjectMocks
    private BotStateHelper testInstance;

    @Test
    public void shouldProcessInputMessage() {
        when(messageMock.getFrom().getId()).thenReturn(USER_ID);
        when(commandFactoryMock.getCommand(BOT_STATE)).thenReturn(botCommandMock);

        testInstance.processInputMessage(BOT_STATE, messageMock);

        verify(botCommandMock).execute(USER_ID, messageMock);
    }

    @Test
    public void shouldProcessInputMessageWithProvidedUserId() {
        when(commandFactoryMock.getCommand(BOT_STATE)).thenReturn(botCommandMock);

        testInstance.processInputMessage(BOT_STATE, USER_ID, messageMock);

        verify(botCommandMock).execute(USER_ID, messageMock);
        verify(messageMock, never()).getFrom();
    }

}
