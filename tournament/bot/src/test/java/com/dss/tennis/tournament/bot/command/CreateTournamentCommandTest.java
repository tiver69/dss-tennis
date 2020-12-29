package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.helper.KeyboardMarkupHelper;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;

@ExtendWith(MockitoExtension.class)
public class CreateTournamentCommandTest {
    public static final long CHAT_ID = 1;
    public static final int USER_ID = 11;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Message messageMock;

    @Mock
    private DssTennisUserStateCache userStateCacheMock;
    @Mock
    private KeyboardMarkupHelper keyboardMarkupHelperMock;

    @InjectMocks
    private CreateTournamentCommand testInstance;

    @Test
    public void shouldExecuteCreateTournamentCommand() {
//        testInstance.execute(USER_ID, messageMock);
    }

}
