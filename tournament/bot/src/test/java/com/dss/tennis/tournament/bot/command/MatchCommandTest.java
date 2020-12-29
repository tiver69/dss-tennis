package com.dss.tennis.tournament.bot.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@ExtendWith(MockitoExtension.class)
public class MatchCommandTest {

    public static final int USER_ID = 1;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message messageMock;

    private MatchCommand testInstance = new MatchCommand();

    @Test
    public void shouldExecuteMatchCommand() {
        SendMessage result = testInstance.execute(USER_ID, messageMock);

        Assertions.assertNull(result);
    }
}
