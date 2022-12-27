package ua.com.dss.tennis.tournament.api.helper.factory;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.model.db.v1.Tournament;
import ua.com.dss.tennis.tournament.api.model.dto.DoubleContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.SingleContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

@ExtendWith(MockitoExtension.class)
class TournamentFactoryTest {

    private static final Integer TOURNAMENT_ID = 1;

    @Mock
    private ConverterHelper converterHelperMock;
    @Mock
    private RoundContestFactory roundTournamentFactoryMock;
    @Mock
    private EliminationContestFactory eliminationTournamentFactoryMock;
    @Spy
    private Tournament tournamentSpy;
    @Spy
    private TournamentDTO tournamentDtoSpy;
    @Spy
    private PlayerDTO playerOneDtoSpy;
    @Spy
    private PlayerDTO playerTwoDtoSpy;
    @Spy
    private SingleContestDTO singleContestOneDtoSpy;
    @Spy
    private SingleContestDTO singleContestTwoDtoSpy;
    @Spy
    private DoubleContestDTO doubleContestOneDtoSpy;
    @Spy
    private DoubleContestDTO doubleContestTwoDtoSpy;


    @InjectMocks
    private TournamentFactory testInstance;

//    @Test
//    public void shouldCreateNewContestsForRoundTournament() {
//        List<PlayerDTO> players = Lists.newArrayList(playerOneDtoSpy, playerTwoDtoSpy);
//
//        testInstance.createContestsForTournament(tournamentSpy, players, TournamentType.ROUND);
//
//        verify(roundTournamentFactoryMock).createContests(tournamentSpy, players);
//    }

//    @Test
//    public void shouldCreateNewContestsForEliminationTournament() {
//        List<PlayerDTO> players = Lists.newArrayList(playerOneDtoSpy, playerTwoDtoSpy);
//
//        testInstance.createContestsForTournament(tournamentSpy, players, TournamentType.ELIMINATION);
//
//        verify(eliminationTournamentFactoryMock).createContests(tournamentSpy, players);
//    }

//    @Test
//    public void shouldPopulateRoundTournamentDtoWithContestsAndSingleParticipants() {
//        List<PlayerDTO> players = Lists.newArrayList(playerOneDtoSpy, playerTwoDtoSpy);
//        List<ContestDTO> contests = Lists.newArrayList(singleContestOneDtoSpy, singleContestTwoDtoSpy);
//        when(converterHelperMock.convert(tournamentSpy, TournamentDTO.class)).thenReturn(tournamentDtoSpy);
//        when(roundTournamentFactoryMock.getContestDTOs(TOURNAMENT_ID, SingleContestDTO.class))
//                .thenReturn(contests);
//        when(singleParticipantFactoryMock.getTournamentPlayers(TOURNAMENT_ID))
//                .thenReturn(players);
//        when(tournamentDtoSpy.getId()).thenReturn(TOURNAMENT_ID);
//        when(tournamentDtoSpy.getParticipantType()).thenReturn(ParticipantType.SINGLE);
//        when(tournamentDtoSpy.getTournamentType()).thenReturn(TournamentType.ROUND);
//
//        TournamentDTO result = testInstance.populateTournamentDTO(tournamentSpy, new RequestParameter(true, true));
//
//        verify(roundTournamentFactoryMock).getContestDTOs(TOURNAMENT_ID, SingleContestDTO.class);
//        verify(tournamentDtoSpy).setContests(contests);
//        verify(singleParticipantFactoryMock).getTournamentPlayers(TOURNAMENT_ID);
//        verify(tournamentDtoSpy).setPlayers(players);
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertTrue(result.getPlayers().containsAll(players)),
//                () -> assertTrue(result.getContests().containsAll(contests))
//        );
//    }
//
//    @Test
//    public void shouldPopulateEliminationTournamentDtoWithContestsAndDoubleParticipants() {
//        List<PlayerDTO> players = Lists.newArrayList(playerOneDtoSpy, playerTwoDtoSpy);
//        List<ContestDTO> contests = Lists.newArrayList(doubleContestOneDtoSpy, doubleContestTwoDtoSpy);
//        when(converterHelperMock.convert(tournamentSpy, TournamentDTO.class)).thenReturn(tournamentDtoSpy);
//        when(eliminationTournamentFactoryMock.getContestDTOs(TOURNAMENT_ID, DoubleContestDTO.class))
//                .thenReturn(contests);
//        when(doubleParticipantFactoryMock.getTournamentPlayers(TOURNAMENT_ID))
//                .thenReturn(players);
//        when(tournamentDtoSpy.getId()).thenReturn(TOURNAMENT_ID);
//        when(tournamentDtoSpy.getParticipantType()).thenReturn(ParticipantType.DOUBLE);
//        when(tournamentDtoSpy.getTournamentType()).thenReturn(TournamentType.ELIMINATION);
//
//        TournamentDTO result = testInstance.populateTournamentDTO(tournamentSpy, new RequestParameter(true, true));
//
//        verify(eliminationTournamentFactoryMock).getContestDTOs(TOURNAMENT_ID, DoubleContestDTO.class);
//        verify(tournamentDtoSpy).setContests(contests);
//        verify(doubleParticipantFactoryMock).getTournamentPlayers(TOURNAMENT_ID);
//        verify(tournamentDtoSpy).setPlayers(players);
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertTrue(result.getPlayers().containsAll(players)),
//                () -> assertTrue(result.getContests().containsAll(contests))
//        );
//    }
//
//    @Test
//    public void shouldPopulateEliminationTournamentDtoWithoutContestsAndParticipants() {
//        List<PlayerDTO> players = Lists.newArrayList(playerOneDtoSpy, playerTwoDtoSpy);
//        List<ContestDTO> contests = Lists.newArrayList(doubleContestOneDtoSpy, doubleContestTwoDtoSpy);
//        when(converterHelperMock.convert(tournamentSpy, TournamentDTO.class)).thenReturn(tournamentDtoSpy);
//
//        TournamentDTO result = testInstance.populateTournamentDTO(tournamentSpy, new RequestParameter(false, false));
//
//        verify(eliminationTournamentFactoryMock, never()).getContestDTOs(TOURNAMENT_ID, DoubleContestDTO.class);
//        verify(tournamentDtoSpy, never()).setContests(contests);
//        verify(doubleParticipantFactoryMock, never()).getTournamentPlayers(TOURNAMENT_ID);
//        verify(tournamentDtoSpy, never()).setPlayers(players);
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertNull(result.getPlayers()),
//                () -> assertNull(result.getContests())
//        );
//    }
}