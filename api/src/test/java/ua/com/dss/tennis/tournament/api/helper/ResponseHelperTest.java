package ua.com.dss.tennis.tournament.api.helper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.exception.handler.WarningHandler;
import ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ResponseHelperTest {

    private static final String TOURNAMENT_NAME = "tournamentName";

    @Mock
    private ConverterHelper converterHelperMock;
    @Mock
    private WarningHandler warningHandlerMock;
//    @Spy
//    private GetTournament getTournamentSpy;
    @Spy
    private PlayerDTO playerOneDtoSpy;
//    @Spy
//    private ResourceObject playerOneResourceObjectSpy;
    @Spy
    private PlayerDTO playerTwoDtoSpy;
//    @Spy
//    private ResourceObject playerTwoResourceObjectSpy;
    @Spy
    private ErrorDataDTO errorDataSpy;

    @InjectMocks
    private ResponseHelper testInstance;

//    @Test
//    public void shouldConvertTournamentDtoWithWarningsAndIncluded() {
//        TournamentDTO tournamentDTO = prepareTournamentDto();
//        when(converterHelperMock.convert(playerOneDtoSpy, ResourceObject.class)).thenReturn(playerOneResourceObjectSpy);
//        when(converterHelperMock.convert(playerTwoDtoSpy, ResourceObject.class)).thenReturn(playerTwoResourceObjectSpy);
//        when(converterHelperMock.convert(tournamentDTO, GetTournament.class)).thenReturn(getTournamentSpy);
//
//        SuccessResponse<GetTournament> result = testInstance
//                .createSuccessResponse(tournamentDTO, Set.of(errorDataSpy));
//
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertEquals(result.getData(), getTournamentSpy),
//                () -> assertEquals(result.getIncluded().size(), 2),
//                () -> assertTrue(result.getIncluded().contains(playerOneResourceObjectSpy)),
//                () -> assertTrue(result.getIncluded().contains(playerTwoResourceObjectSpy)),
//                () -> assertFalse(result.getWarnings().isEmpty())
//        );
//    }

//    @Test
//    public void shouldConvertTournamentDtoWithWarningsWithoutIncluded() {
//        TournamentDTO tournamentDTO = prepareTournamentDto();
//        tournamentDTO.setPlayers(List.of());
//        when(converterHelperMock.convert(tournamentDTO, GetTournament.class)).thenReturn(getTournamentSpy);
//
//        SuccessResponse<GetTournament> result = testInstance
//                .createSuccessResponse(tournamentDTO, Set.of(errorDataSpy));
//
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertEquals(result.getData(), getTournamentSpy),
//                () -> assertNull(result.getIncluded()),
//                () -> assertFalse(result.getWarnings().isEmpty())
//        );
//    }

//    @Test
//    public void shouldConvertSuccessfulResponseWithWarningsWithoutIncluded() {
//        TournamentDTO tournamentDTO = prepareTournamentDto();
//        when(converterHelperMock.convert(tournamentDTO, GetTournament.class)).thenReturn(getTournamentSpy);
//        SuccessResponseDTO<TournamentDTO> data = new SuccessResponseDTO<>(tournamentDTO, Set.of(errorDataSpy));
//
//        SuccessResponse<GetTournament> result = testInstance
//                .createSuccessResponse(data, GetTournament.class);
//
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertEquals(result.getData(), getTournamentSpy),
//                () -> assertNull(result.getIncluded()),
//                () -> assertFalse(result.getWarnings().isEmpty())
//        );
//    }

//    @Test
//    public void shouldConvertSuccessfulResponseWithoutWarningsWithoutIncluded() {
//        TournamentDTO tournamentDTO = prepareTournamentDto();
//        when(converterHelperMock.convert(tournamentDTO, GetTournament.class)).thenReturn(getTournamentSpy);
//
//        SuccessResponse<GetTournament> result = testInstance.createSuccessResponse(tournamentDTO, GetTournament.class);
//
//        Assertions.assertAll(
//                () -> assertNotNull(result),
//                () -> assertEquals(result.getData(), getTournamentSpy),
//                () -> assertNull(result.getIncluded()),
//                () -> assertNull(result.getWarnings())
//        );
//    }

    private TournamentDTO prepareTournamentDto() {
        List<PlayerDTO> player = new ArrayList<>();
        player.add(playerOneDtoSpy);
        player.add(playerTwoDtoSpy);

        return TournamentDTO.builder()
                .name(TOURNAMENT_NAME)
                .tournamentType(TournamentType.ROUND)
                .players(player)
                .build();
    }
}