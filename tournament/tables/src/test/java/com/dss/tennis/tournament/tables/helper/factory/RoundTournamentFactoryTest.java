package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Contest;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoundTournamentFactoryTest {

    private static final int TOURNAMENT_ID = 1;

    @Mock
    private PlayerHelper playerHelperMock;
    @Mock
    private ContestHelper contestHelperMock;
    @Mock
    private ConverterHelper converterHelperMock;

    @Spy
    private Tournament tournamentSpy;
    @Spy
    private TournamentDTO tournamentDtoSpy;
    @Spy
    private PlayerDTO playerOneDtoSpy;
    @Spy
    private Player playerOneSpy;
    @Spy
    private PlayerDTO playerTwoDtoSpy;
    @Spy
    private Player playerTwoSpy;
    @Spy
    private Contest contestOneSpy;
    @Spy
    private Contest contestTwoSpy;
    @Spy
    private ContestDTO contestOneDtoSpy;
    @Spy
    private ContestDTO contestTwoDtoSpy;

    @InjectMocks
    private RoundTournamentFactory testInstance;

    @Test
    public void shouldBuildExistingRoundTournament() {
        when(contestHelperMock.getTournamentContests(TOURNAMENT_ID))
                .thenReturn(Arrays.asList(contestOneSpy, contestTwoSpy));
        when(converterHelperMock.convert(contestOneSpy, ContestDTO.class)).thenReturn(contestOneDtoSpy);
        when(converterHelperMock.convert(contestTwoSpy, ContestDTO.class)).thenReturn(contestTwoDtoSpy);

        TournamentDTO result = new TournamentDTO();
        result.setId(TOURNAMENT_ID);
        testInstance.buildExistingTournament(result);

        Assertions.assertAll(
                () -> assertNotNull(result.getContests()),
                () -> assertTrue(result.getContests().contains(contestOneDtoSpy)),
                () -> assertTrue(result.getContests().contains(contestTwoDtoSpy))
        );
    }

    @Test
    public void shouldCreateNewContestsForRoundTournament() {
        when(playerHelperMock.getPlayer(playerOneDtoSpy)).thenReturn(playerOneSpy);
        when(playerHelperMock.getPlayer(playerTwoDtoSpy)).thenReturn(playerTwoSpy);

        Tournament result = testInstance.createNewContests(tournamentSpy, Lists.list(playerOneDtoSpy, playerTwoDtoSpy));

        assertEquals(result, tournamentSpy);
        verify(contestHelperMock).createNewContest(playerOneSpy, playerTwoSpy, tournamentSpy);
    }

}