package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.model.v1.Contest;
import com.dss.tennis.tournament.tables.model.v1.Player;
import com.dss.tennis.tournament.tables.model.v1.Tournament;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContestHelperTest {

    @Mock
    private ContestRepository contestRepository;
    @Spy
    private Player playerOne;
    @Spy
    private Player playerTwo;
    @Spy
    private Tournament tournament;

    @InjectMocks
    private ContestHelper testInstance;

    @Test
    public void shouldCreateNewContest() {
        testInstance.createNewContest(playerOne, playerTwo, tournament);

        verify(contestRepository).save(any(Contest.class));
    }
}