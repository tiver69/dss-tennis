package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoundTournamentFactoryTest {

    @Mock
    private PlayerHelper playerHelperMock;
    @Mock
    private ContestHelper contestHelperMock;

    @Spy
    private Tournament tournamentSpy;
    @Spy
    private PlayerDTO playerOneDtoSpy;
    @Spy
    private Player playerOneSpy;
    @Spy
    private PlayerDTO playerTwoDtoSpy;
    @Spy
    private Player playerTwoSpy;

    @InjectMocks
    private RoundTournamentFactory testInstance;

    @Test
    public void shouldThrowUnsupportedError() {
        when(playerHelperMock.getPlayer(playerOneDtoSpy)).thenReturn(playerOneSpy);
        when(playerHelperMock.getPlayer(playerTwoDtoSpy)).thenReturn(playerTwoSpy);

        Tournament result = testInstance.build(tournamentSpy, Lists.list(playerOneDtoSpy, playerTwoDtoSpy));

        Assertions.assertEquals(result, tournamentSpy);
        verify(contestHelperMock).createNewContest(playerOneSpy, playerTwoSpy, tournamentSpy);
    }

}