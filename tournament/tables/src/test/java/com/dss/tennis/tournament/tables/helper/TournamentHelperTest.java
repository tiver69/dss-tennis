package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.factory.EliminationTournamentFactory;
import com.dss.tennis.tournament.tables.model.db.v1.*;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import com.dss.tennis.tournament.tables.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NOT_FOUND;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentHelperTest {

    private static final int TOURNAMENT_ID = 1;

    @Mock
    private PlayerRepository playerRepositoryMock;
    @Mock
    private TournamentRepository tournamentRepositoryMock;
    @Mock
    private EliminationTournamentFactory eliminationTournamentFactoryMock;
    @Mock
    private ConverterHelper converterHelperMock;
    @Spy
    private Player playerSpy;
    @Spy
    private Tournament tournamentSpy;
    @Spy
    private TournamentDTO tournamentDtoSpy;

    @InjectMocks
    private TournamentHelper testInstance;

    @Test
    public void shouldCreateNewEliminationTournament() {
        List<PlayerDTO> players = Collections.emptyList();
        when(tournamentDtoSpy.getTournamentType()).thenReturn(TournamentType.ELIMINATION);
        when(tournamentDtoSpy.getPlayers()).thenReturn(players);
        when(tournamentRepositoryMock.save(any(Tournament.class))).thenReturn(tournamentSpy);

        Tournament result = testInstance.createNewTournamentWithContests(tournamentDtoSpy);

        Assertions.assertEquals(tournamentSpy, result);
        verify(tournamentRepositoryMock).save(any(Tournament.class));
        verify(eliminationTournamentFactoryMock).createNewContests(tournamentSpy, players);
    }

    @Test
    public void shouldGetEliminationTournamentWithContestsWithoutPlayers() {
        when(tournamentSpy.getTournamentType()).thenReturn(TournamentType.ELIMINATION);
        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournamentSpy));
        when(converterHelperMock.convert(tournamentSpy, TournamentDTO.class)).thenReturn(tournamentDtoSpy);

        TournamentDTO result = testInstance.getTournament(TOURNAMENT_ID, RequestParameter.DEFAULT);

        Assertions.assertEquals(tournamentDtoSpy, result);
        verify(eliminationTournamentFactoryMock).buildExistingTournament(tournamentDtoSpy);
        verify(playerRepositoryMock, never()).findPlayersBySingleTournamentId(TOURNAMENT_ID);
    }

    @Test
    public void shouldGetEliminationTournamentWithoutContestsWithPlayers() {
        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournamentSpy));
        when(converterHelperMock.convert(tournamentSpy, TournamentDTO.class)).thenReturn(tournamentDtoSpy);
        when(playerRepositoryMock.findPlayersBySingleTournamentId(TOURNAMENT_ID)).thenReturn(List.of(playerSpy));

        TournamentDTO result = testInstance.getTournament(TOURNAMENT_ID, new RequestParameter(false, true));

        Assertions.assertEquals(tournamentDtoSpy, result);
        verify(eliminationTournamentFactoryMock, never()).buildExistingTournament(tournamentDtoSpy);
        verify(playerRepositoryMock).findPlayersBySingleTournamentId(TOURNAMENT_ID);
        verify(converterHelperMock).convert(playerSpy, PlayerDTO.class);
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
                .participantType(ParticipantType.SINGLE)
                .build();
    }

    private Tournament prepareInProgressTournament() {
        return Tournament.builder().status(StatusType.IN_PROGRESS).beginningDate(LocalDate.now())
                .participantType(ParticipantType.SINGLE).build();
    }
}
