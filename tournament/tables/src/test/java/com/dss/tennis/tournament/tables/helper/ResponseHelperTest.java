package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.response.v1.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseHelperTest {

    private static final String TOURNAMENT_NAME = "tournamentName";

    @Mock
    private ConverterHelper converterHelperMock;
    @Spy
    private GetTournament<GetSingleParticipant> getTournamentSpy;
    @Spy
    private PlayerDTO playerOneDtoSpy;
    @Spy
    private ResourceObject playerOneResourceObjectSpy;
    @Spy
    private PlayerDTO playerTwoDtoSpy;
    @Spy
    private ResourceObject playerTwoResourceObjectSpy;
    @Spy
    private ErrorData errorDataSpy;

    @InjectMocks
    private ResponseHelper testInstance;

    @Test
    public void shouldConvertTournamentDtoWithWarningsAndIncluded() {
        TournamentDTO tournamentDTO = prepareTournamentDto();
        when(converterHelperMock.convert(playerOneDtoSpy, ResourceObject.class)).thenReturn(playerOneResourceObjectSpy);
        when(converterHelperMock.convert(playerTwoDtoSpy, ResourceObject.class)).thenReturn(playerTwoResourceObjectSpy);
        when(converterHelperMock.convert(tournamentDTO, GetTournament.class)).thenReturn(getTournamentSpy);

        SuccessResponse<GetTournament> result = testInstance
                .createSuccessResponse(tournamentDTO, List.of(errorDataSpy));

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result.getData(), getTournamentSpy),
                () -> assertEquals(result.getIncluded().size(), 2),
                () -> assertTrue(result.getIncluded().contains(playerOneResourceObjectSpy)),
                () -> assertTrue(result.getIncluded().contains(playerTwoResourceObjectSpy)),
                () -> assertTrue(result.getWarnings().contains(errorDataSpy))
        );
    }

    @Test
    public void shouldConvertTournamentDtoWithWarningsWithoutIncluded() {
        TournamentDTO tournamentDTO = prepareTournamentDto();
        tournamentDTO.setPlayers(List.of());
        when(converterHelperMock.convert(tournamentDTO, GetTournament.class)).thenReturn(getTournamentSpy);

        SuccessResponse<GetTournament> result = testInstance
                .createSuccessResponse(tournamentDTO, List.of(errorDataSpy));

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result.getData(), getTournamentSpy),
                () -> assertNull(result.getIncluded()),
                () -> assertTrue(result.getWarnings().contains(errorDataSpy))
        );
    }

    @Test
    public void shouldConvertSuccessfulResponseWithWarningsWithoutIncluded() {
        TournamentDTO tournamentDTO = prepareTournamentDto();
        when(converterHelperMock.convert(tournamentDTO, GetTournament.class)).thenReturn(getTournamentSpy);
        SuccessResponseDTO<TournamentDTO> data = new SuccessResponseDTO<>(tournamentDTO, List.of(errorDataSpy));

        SuccessResponse<GetTournament> result = testInstance
                .createSuccessResponse(data, GetTournament.class);

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result.getData(), getTournamentSpy),
                () -> assertNull(result.getIncluded()),
                () -> assertTrue(result.getWarnings().contains(errorDataSpy))
        );
    }

    @Test
    public void shouldConvertSuccessfulResponseWithoutWarningsWithoutIncluded() {
        TournamentDTO tournamentDTO = prepareTournamentDto();
        when(converterHelperMock.convert(tournamentDTO, GetTournament.class)).thenReturn(getTournamentSpy);

        SuccessResponse<GetTournament> result = testInstance.createSuccessResponse(tournamentDTO, GetTournament.class);

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result.getData(), getTournamentSpy),
                () -> assertNull(result.getIncluded()),
                () -> assertNull(result.getWarnings())
        );
    }

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