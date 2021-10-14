package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.factory.EliminationTournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
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
import java.util.Optional;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentHelperTest {

    private static final int TOURNAMENT_ID = 1;

    @Mock
    private TournamentRepository tournamentRepositoryMock;
    @Mock
    private EliminationTournamentFactory eliminationTournamentFactoryMock;
    @Spy
    private Tournament tournamentSpy;
    @Spy
    private TournamentDTO tournamentDtoSpy;

    @InjectMocks
    private TournamentHelper testInstance;

    @Test
    public void shouldCreateNewEliminationTournament() {
        List<PlayerDTO> players = Collections.emptyList();
        when(tournamentDtoSpy.getType()).thenReturn(TournamentType.ELIMINATION);
        when(tournamentDtoSpy.getPlayers()).thenReturn(players);
        when(tournamentRepositoryMock.save(any(Tournament.class))).thenReturn(tournamentSpy);
        Tournament result = testInstance.createNewTournamentWithContests(tournamentDtoSpy);

        Assertions.assertEquals(tournamentSpy, result);
        verify(tournamentRepositoryMock).save(any(Tournament.class));
        verify(eliminationTournamentFactoryMock).createNewContests(tournamentSpy, players);
    }

    @Test
    public void shouldGetEliminationTournament() {
        when(tournamentSpy.getType()).thenReturn(TournamentType.ELIMINATION);
        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournamentSpy));
        when(eliminationTournamentFactoryMock.buildExistingTournament(tournamentSpy)).thenReturn(tournamentDtoSpy);
        TournamentDTO result = testInstance.getTournament(TOURNAMENT_ID);

        Assertions.assertEquals(tournamentDtoSpy, result);
    }

    @Test
    public void shouldThrowExceptionIfTournamentNotExist() {
        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.empty());

        DetailedException resultError = Assertions
                .assertThrows(DetailedException.class, () -> testInstance.getTournament(TOURNAMENT_ID));
        Set<DetailedErrorData> result = resultError.getErrors();

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream().map(DetailedErrorData::getErrorConstant)
                        .anyMatch(tt -> tt.equals(TOURNAMENT_NOT_FOUND)))
        );
    }

    @Test
    public void shouldCreateNewTournament() {
        testInstance.createNewTournament(tournamentDtoSpy);

        verify(tournamentRepositoryMock).save(any(Tournament.class));
    }
}