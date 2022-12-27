package ua.com.dss.tennis.tournament.api.helper.factory;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.helper.ContestHelper;
import ua.com.dss.tennis.tournament.api.helper.participant.PlayerHelper;
import ua.com.dss.tennis.tournament.api.model.db.v1.Player;
import ua.com.dss.tennis.tournament.api.model.db.v1.Tournament;
import ua.com.dss.tennis.tournament.api.model.db.v2.Contest;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.SingleContestDTO;

@ExtendWith(MockitoExtension.class)
class RoundContestFactoryTest {

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
    private SingleContestDTO contestOneDtoSpy;
    @Spy
    private SingleContestDTO contestTwoDtoSpy;

    @InjectMocks
    private RoundContestFactory testInstance;

//    @Test
//    public void shouldCreateNewContestsForRoundTournament() {
//        when(playerHelperMock.getPlayer(playerOneDtoSpy)).thenReturn(playerOneSpy);
//        when(playerHelperMock.getPlayer(playerTwoDtoSpy)).thenReturn(playerTwoSpy);
//
//        testInstance.createContests(tournamentSpy, Lists.list(playerOneDtoSpy, playerTwoDtoSpy));
//
//        verify(contestHelperMock).createNewContest(playerOneSpy, playerTwoSpy, tournamentSpy);
//    }

//    @Test
//    public void shouldGerContestDTOsForSingleParticipantTournament() {
//        when(contestHelperMock.getTournamentContests(TOURNAMENT_ID))
//                .thenReturn(Arrays.asList(contestOneSpy, contestTwoSpy));
//        when(converterHelperMock.convert(contestOneSpy, SingleContestDTO.class)).thenReturn(contestOneDtoSpy);
//        when(converterHelperMock.convert(contestTwoSpy, SingleContestDTO.class)).thenReturn(contestTwoDtoSpy);
//
//        List<ContestDTO> result = testInstance.getContestDTOs(TOURNAMENT_ID);
//
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertTrue(result.contains(contestOneDtoSpy)),
//                () -> assertTrue(result.contains(contestTwoDtoSpy))
//        );
//    }

}