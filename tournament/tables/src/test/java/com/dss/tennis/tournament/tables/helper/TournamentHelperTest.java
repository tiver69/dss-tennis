package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.factory.TournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.StatusType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NOT_FOUND;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentHelperTest {

    private static final int TOURNAMENT_ID = 1;

    @Mock
    private TournamentFactory tournamentFactoryMock;
    @Mock
    private TournamentRepository tournamentRepositoryMock;
    @Spy
    private RequestParameter requestParameterSpy;
    @Spy
    private Tournament tournamentSpy;
    @Spy
    private TournamentDTO tournamentDtoSpy;

    @InjectMocks
    private TournamentHelper testInstance;

//    @Test
//    public void shouldCreateNewEliminationTournament() {
//        List<PlayerDTO> players = Collections.emptyList();
//        when(tournamentDtoSpy.getTournamentType()).thenReturn(TournamentType.ELIMINATION);
//        when(tournamentDtoSpy.getPlayers()).thenReturn(players);
//        when(tournamentRepositoryMock.save(any(Tournament.class))).thenReturn(tournamentSpy);
//
//        Tournament result = testInstance.createNewTournamentWithContests(tournamentDtoSpy);
//
//        Assertions.assertEquals(tournamentSpy, result);
//        verify(tournamentRepositoryMock).save(any(Tournament.class));
//        verify(tournamentFactoryMock).createContestsForTournament(tournamentSpy, players, TournamentType.ELIMINATION);
//    }

    @Test
    public void shouldGetEliminationTournamentWithDefaultRequestParameter() {
        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournamentSpy));
        when(tournamentFactoryMock.populateTournamentDTO(tournamentSpy, RequestParameter.DEFAULT))
                .thenReturn(tournamentDtoSpy);

        TournamentDTO result = testInstance.getTournament(TOURNAMENT_ID);

        Assertions.assertEquals(tournamentDtoSpy, result);
        verify(tournamentFactoryMock).populateTournamentDTO(tournamentSpy, RequestParameter.DEFAULT);
    }

    @Test
    public void shouldGetEliminationTournamentWithCustomRequestParameter() {
        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournamentSpy));
        when(tournamentFactoryMock.populateTournamentDTO(tournamentSpy, requestParameterSpy))
                .thenReturn(tournamentDtoSpy);

        TournamentDTO result = testInstance.getTournament(TOURNAMENT_ID, requestParameterSpy);

        Assertions.assertEquals(tournamentDtoSpy, result);
        verify(tournamentFactoryMock).populateTournamentDTO(tournamentSpy, requestParameterSpy);
    }

    @Test
    public void shouldThrowExceptionIfTournamentNotExist() {
        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.empty());

        DetailedException resultError = Assertions
                .assertThrows(DetailedException.class, () -> testInstance
                        .getTournament(TOURNAMENT_ID, RequestParameter.DEFAULT));
        Set<DetailedErrorData> result = resultError.getErrors();

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream().map(DetailedErrorData::getErrorConstant)
                        .anyMatch(tt -> tt.equals(TOURNAMENT_NOT_FOUND)))
        );
    }

    @Test
    public void shouldCreateNewTournamentWithBeginningDateInFuture() {
        when(tournamentDtoSpy.getBeginningDate()).thenReturn(LocalDate.now().plus(1, DAYS));

        testInstance.createNewTournament(tournamentDtoSpy);

        verify(tournamentRepositoryMock).save(preparePlannedTournament());
    }

    @Test
    public void shouldCreateNewTournamentWithCurrentBeginningDate() {
        when(tournamentDtoSpy.getBeginningDate()).thenReturn(LocalDate.now());

        testInstance.createNewTournament(tournamentDtoSpy);

        verify(tournamentRepositoryMock).save(prepareInProgressTournament());
    }

    private Tournament preparePlannedTournament() {
        return Tournament.builder().status(StatusType.PLANNED).beginningDate(LocalDate.now().plus(1, DAYS))
                .build();
    }

    private Tournament prepareInProgressTournament() {
        return Tournament.builder().status(StatusType.IN_PROGRESS).beginningDate(LocalDate.now()).build();
    }
}
