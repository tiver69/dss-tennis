package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.assertj.core.util.Lists;
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
    @Mock
    private TournamentValidator tournamentValidatorMock;
    @Mock
    private PlayerValidator playerValidatorMock;
    @Mock
    private TournamentHelper tournamentHelperMock;

    @Spy
    private CreateTournamentDTO createTournamentDtoSpy;
    @Spy
    private PlayerDTO playerDtoSpy;
    @Spy
    private Tournament tournamentSpy;

    @InjectMocks
    private TournamentService testInstance;

    @Test
    public void shouldCreateNewTournament() {
        when(createTournamentDtoSpy.getPlayers()).thenReturn(Lists.list(playerDtoSpy));
        when(tournamentHelperMock.createNewTournamentWithContests(createTournamentDtoSpy)).thenReturn(tournamentSpy);

        testInstance.createNewTournament(createTournamentDtoSpy);

        verify(tournamentValidatorMock).validateCreateTournament(createTournamentDtoSpy);
        verify(playerValidatorMock).validatePlayer(playerDtoSpy);
        verify(tournamentHelperMock).createNewTournamentWithContests(createTournamentDtoSpy);
    }

    @Test
    public void shouldReturnExceptionWhenValidationFailOnCreateNewTournament() {
        when(createTournamentDtoSpy.getPlayers()).thenReturn(Lists.list(playerDtoSpy));
        when(playerValidatorMock.validatePlayer(playerDtoSpy))
                .thenReturn(Sets.newSet(new DetailedErrorData()));

        DetailedException result = assertThrows(DetailedException.class, () -> testInstance
                .createNewTournament(createTournamentDtoSpy));

        assertEquals(1, result.getErrors().size());
        assertFalse(result.getErrors().isEmpty());
        verify(tournamentValidatorMock).validateCreateTournament(createTournamentDtoSpy);
        verify(playerValidatorMock).validatePlayer(playerDtoSpy);
    }
}