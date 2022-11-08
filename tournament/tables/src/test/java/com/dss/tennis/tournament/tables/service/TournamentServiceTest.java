package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorResponse.ErrorData;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import com.dss.tennis.tournament.tables.validator.participant.PlayerValidator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    private static final int TOURNAMENT_ID = 1;
    private static final Byte REMOVED_SEQUENTIAL = 1;

    @Mock
    private TournamentValidator tournamentValidatorMock;
    @Mock
    private PlayerValidator playerValidatorMock;
    @Mock
    private PlayerHelper playerHelperMock;
    @Mock
    private TournamentHelper tournamentHelperMock;
    @Mock
    private WarningHandler warningHandlerMock;

    @Spy
    private TournamentDTO tournamentDtoSpy;
    @Spy
    private TournamentDTO responseTournamentDtoSpy;
    @Spy
    private PlayerDTO playerDtoSpy;
    @Spy
    private PlayerDTO removedPlayerDtoSpy;
    @Spy
    private ErrorData errorDataSpy;
    @Spy
    private Tournament tournamentSpy;

    @InjectMocks
    private TournamentService testInstance;

//    @Test
//    public void shouldCreateNewTournamentWithPlayerDuplication() {
//        List<PlayerDTO> players = Lists.list(playerDtoSpy);
//        when(removedPlayerDtoSpy.getSequenceNumber()).thenReturn(REMOVED_SEQUENTIAL);
//        when(tournamentSpy.getId()).thenReturn(TOURNAMENT_ID);
//        when(tournamentDtoSpy.getPlayers()).thenReturn(players);
//        when(tournamentHelperMock.createNewTournamentWithContests(tournamentDtoSpy)).thenReturn(tournamentSpy);
//        when(tournamentHelperMock.getTournament(TOURNAMENT_ID))
//                .thenReturn(responseTournamentDtoSpy);
//        when(playerHelperMock.removePlayerDuplicates(players)).thenReturn(Lists.list(removedPlayerDtoSpy));
//        when(warningHandlerMock.createWarning(PLAYER_DUPLICATION, REMOVED_SEQUENTIAL)).thenReturn(errorDataSpy);
//
//        SuccessResponseDTO<TournamentDTO> result = testInstance.createNewTournament(tournamentDtoSpy);
//
//        Assertions.assertAll(
//                () -> assertEquals(responseTournamentDtoSpy, result.getData()),
//                () -> assertFalse(result.getWarnings().isEmpty()),
//                () -> assertTrue(result.getWarnings().contains(errorDataSpy))
//        );
//
//        verify(tournamentValidatorMock).validateCreateTournament(tournamentDtoSpy);
//        verify(playerValidatorMock).validatePlayer(playerDtoSpy);
//    }

//    @Test
//    public void shouldCreateNewTournamentWithoutPlayerDuplication() {
//        List<PlayerDTO> players = Lists.list(playerDtoSpy);
//        when(tournamentSpy.getId()).thenReturn(TOURNAMENT_ID);
//        when(tournamentDtoSpy.getPlayers()).thenReturn(players);
//        when(tournamentHelperMock.createNewTournamentWithContests(tournamentDtoSpy)).thenReturn(tournamentSpy);
//        when(tournamentHelperMock.getTournament(TOURNAMENT_ID))
//                .thenReturn(responseTournamentDtoSpy);
//        when(playerHelperMock.removePlayerDuplicates(players)).thenReturn(Lists.emptyList());
//
//        SuccessResponseDTO<TournamentDTO> result = testInstance.createNewTournament(tournamentDtoSpy);
//
//        Assertions.assertAll(
//                () -> assertEquals(responseTournamentDtoSpy, result.getData()),
//                () -> assertTrue(result.getWarnings().isEmpty())
//        );
//
//        verify(tournamentValidatorMock).validateCreateTournament(tournamentDtoSpy);
//        verify(playerValidatorMock).validatePlayer(playerDtoSpy);
//        verify(warningHandlerMock, never()).createWarning(any(WarningConstant.class), any(Byte.class));
//    }

//    @Test
//    public void shouldGetTournament() {
//        RequestParameter requestParameter = new RequestParameter();
//        when(tournamentHelperMock.getTournamentDto(TOURNAMENT_ID, requestParameter)).thenReturn(responseTournamentDtoSpy);
//
//        Assertions.assertEquals(responseTournamentDtoSpy, testInstance.getTournament(TOURNAMENT_ID, requestParameter));
//    }

//    @Test
//    public void shouldReturnExceptionWhenValidationFailOnCreateNewTournament() {
//        when(tournamentDtoSpy.getPlayers()).thenReturn(Lists.list(playerDtoSpy));
//        when(playerValidatorMock.validatePlayer(playerDtoSpy))
//                .thenReturn(Sets.newSet(new DetailedErrorData()));
//
//        DetailedException result = assertThrows(DetailedException.class, () -> testInstance
//                .createNewTournament(tournamentDtoSpy));
//
//        assertEquals(1, result.getErrors().size());
//        assertFalse(result.getErrors().isEmpty());
//        verify(tournamentValidatorMock).validateCreateTournament(tournamentDtoSpy);
//        verify(playerValidatorMock).validatePlayer(playerDtoSpy);
//    }
}