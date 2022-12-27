package ua.com.dss.tennis.tournament.api.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.helper.factory.TournamentFactory;
import ua.com.dss.tennis.tournament.api.model.db.v1.Tournament;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.RequestParameter;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;
import ua.com.dss.tennis.tournament.api.repository.TournamentRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.TOURNAMENT_NOT_FOUND;

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

//    @Test
//    public void shouldGetEliminationTournamentWithDefaultRequestParameter() {
//        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournamentSpy));
//        when(tournamentFactoryMock.populateTournamentDTO(tournamentSpy, RequestParameter.DEFAULT))
//                .thenReturn(tournamentDtoSpy);
//
//        TournamentDTO result = testInstance.getTournamentDto(TOURNAMENT_ID);
//
//        Assertions.assertEquals(tournamentDtoSpy, result);
//        verify(tournamentFactoryMock).populateTournamentDTO(tournamentSpy, RequestParameter.DEFAULT);
//    }
//
//    @Test
//    public void shouldGetEliminationTournamentWithCustomRequestParameter() {
//        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournamentSpy));
//        when(tournamentFactoryMock.populateTournamentDTO(tournamentSpy, requestParameterSpy))
//                .thenReturn(tournamentDtoSpy);
//
//        TournamentDTO result = testInstance.getTournamentDto(TOURNAMENT_ID, requestParameterSpy);
//
//        Assertions.assertEquals(tournamentDtoSpy, result);
//        verify(tournamentFactoryMock).populateTournamentDTO(tournamentSpy, requestParameterSpy);
//    }

    @Test
    public void shouldThrowExceptionIfTournamentNotExist() {
        when(tournamentRepositoryMock.findById(TOURNAMENT_ID)).thenReturn(Optional.empty());

        DetailedException resultError = Assertions
                .assertThrows(DetailedException.class, () -> testInstance
                        .getTournamentDto(TOURNAMENT_ID));
        Set<ErrorDataDTO> result = resultError.getErrors();

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream().map(ErrorDataDTO::getErrorKey)
                        .anyMatch(tt -> tt.equals(TOURNAMENT_NOT_FOUND)))
        );
    }

//    @Test
//    public void shouldCreateNewTournamentWithBeginningDateInFuture() {
//        when(tournamentDtoSpy.getBeginningDate()).thenReturn(LocalDate.now().plus(1, DAYS));
//
//        testInstance.saveTournament(tournamentDtoSpy);
//
//        verify(tournamentRepositoryMock).save(preparePlannedTournament());
//    }
//
//    @Test
//    public void shouldCreateNewTournamentWithCurrentBeginningDate() {
//        when(tournamentDtoSpy.getBeginningDate()).thenReturn(LocalDate.now());
//
//        testInstance.saveTournament(tournamentDtoSpy);
//
//        verify(tournamentRepositoryMock).save(prepareInProgressTournament());
//    }

//    private Tournament preparePlannedTournament() {
//        return Tournament.builder().status(StatusType.PLANNED).beginningDate(LocalDate.now().plus(1, DAYS))
//                .build();
//    }
//
//    private Tournament prepareInProgressTournament() {
//        return Tournament.builder().status(StatusType.IN_PROGRESS).beginningDate(LocalDate.now()).build();
//    }
}
