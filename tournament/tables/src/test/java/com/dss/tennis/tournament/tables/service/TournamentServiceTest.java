package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    private static final int TOURNAMENT_ID = 1;

    @Mock
    private TournamentValidator tournamentValidatorMock;
    @Mock
    private PlayerValidator playerValidatorMock;
    @Mock
    private TournamentHelper tournamentHelperMock;

    @Spy
    private TournamentDTO tournamentDtoSpy;
    @Spy
    private TournamentDTO responseTournamentDtoSpy;
    @Spy
    private PlayerDTO playerDtoSpy;
    @Spy
    private Tournament tournamentSpy;

    @InjectMocks
    private TournamentService testInstance;

    @Test
    public void shouldCreateNewTournament() {
        when(tournamentSpy.getId()).thenReturn(TOURNAMENT_ID);
        when(tournamentDtoSpy.getPlayers()).thenReturn(Lists.list(playerDtoSpy));
        when(tournamentHelperMock.createNewTournamentWithContests(tournamentDtoSpy)).thenReturn(tournamentSpy);
        when(tournamentHelperMock.getTournament(TOURNAMENT_ID)).thenReturn(responseTournamentDtoSpy);

        TournamentDTO result = testInstance.createNewTournament(tournamentDtoSpy);

        Assertions.assertEquals(responseTournamentDtoSpy, result);
        verify(tournamentValidatorMock).validateCreateTournament(tournamentDtoSpy);
        verify(playerValidatorMock).validatePlayer(playerDtoSpy);
    }

    @Test
    public void shouldGetTournament() {
        when(tournamentHelperMock.getTournament(TOURNAMENT_ID)).thenReturn(responseTournamentDtoSpy);

        Assertions.assertEquals(responseTournamentDtoSpy, testInstance.getTournament(TOURNAMENT_ID));
    }

    @Test
    public void shouldReturnExceptionWhenValidationFailOnCreateNewTournament() {
        when(tournamentDtoSpy.getPlayers()).thenReturn(Lists.list(playerDtoSpy));
        when(playerValidatorMock.validatePlayer(playerDtoSpy))
                .thenReturn(Sets.newSet(new DetailedErrorData()));

        DetailedException result = assertThrows(DetailedException.class, () -> testInstance
                .createNewTournament(tournamentDtoSpy));

        assertEquals(1, result.getErrors().size());
        assertFalse(result.getErrors().isEmpty());
        verify(tournamentValidatorMock).validateCreateTournament(tournamentDtoSpy);
        verify(playerValidatorMock).validatePlayer(playerDtoSpy);
    }
}