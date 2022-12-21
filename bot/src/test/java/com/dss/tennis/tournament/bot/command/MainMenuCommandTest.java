package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.helper.MainMenuHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.dss.tennis.tournament.bot.command.MainMenuCommand.START_MESSAGE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainMenuCommandTest {

    public static final long CHAT_ID = 1;
    public static final int USER_ID = 11;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message messageMock;
    @Mock
    private MainMenuHelper mainMenuHelper;

    @InjectMocks
    private MainMenuCommand testInstance;

    @Test
    public void shouldExecuteMainMenuCommand() {
        when(messageMock.getChatId()).thenReturn(CHAT_ID);

        testInstance.execute(USER_ID, messageMock);

        verify(mainMenuHelper).getMainMenuMessage(CHAT_ID, START_MESSAGE);
    }
}
