package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.helper.KeyboardMarkupHelper;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.dss.tennis.tournament.bot.command.InitCommand.INIT_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InitCommandTest {
    public static final long CHAT_ID = 1;
    public static final int USER_ID = 11;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message messageMock;
    @Mock
    private KeyboardMarkupHelper keyboardMarkupHelperMock;
    @Mock
    private DssTennisUserStateCache userStateCacheMock;

    @InjectMocks
    private InitCommand testInstance;

    @Test
    public void shouldExecuteInitCommand() {
        when(messageMock.getChatId()).thenReturn(CHAT_ID);

        SendMessage result = testInstance.execute(USER_ID, messageMock);

        verify(userStateCacheMock).clearForUser(USER_ID);
        verify(keyboardMarkupHelperMock).getTournamentActionMarkup();
        Assertions.assertAll(
                () -> assertEquals(Long.toString(CHAT_ID), result.getChatId()),
                () -> assertEquals(INIT_TEXT, result.getText())
        );
    }
}
