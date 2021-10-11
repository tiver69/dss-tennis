package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.helper.factory.EliminationTournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentHelperTest {

    @Mock
    private TournamentRepository tournamentRepositoryMock;
    @Mock
    private EliminationTournamentFactory eliminationTournamentFactoryMock;
    @Spy
    private Tournament tournamentSpy;
    @Spy
    private CreateTournamentDTO createTournamentDtoSpy;

    @InjectMocks
    private TournamentHelper testInstance;

    @Test
    public void shouldCreateNewEliminationTournament() {
        List<PlayerDTO> players = Collections.emptyList();
        when(createTournamentDtoSpy.getType()).thenReturn(TournamentType.ELIMINATION);
        when(createTournamentDtoSpy.getPlayers()).thenReturn(players);
        when(tournamentRepositoryMock.save(any(Tournament.class))).thenReturn(tournamentSpy);
        Tournament result = testInstance.createNewTournamentWithContests(createTournamentDtoSpy);

        Assertions.assertEquals(tournamentSpy, result);
        verify(tournamentRepositoryMock).save(any(Tournament.class));
        verify(eliminationTournamentFactoryMock).build(tournamentSpy, players);
    }

    @Test
    public void shouldCreateNewTournament() {
        testInstance.createNewTournament(createTournamentDtoSpy);

        verify(tournamentRepositoryMock).save(any(Tournament.class));
    }
}