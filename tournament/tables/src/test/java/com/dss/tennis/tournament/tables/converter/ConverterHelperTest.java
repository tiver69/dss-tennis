package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.converter.modelmapper.ModelMapperFactory;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConverterHelperTest {

    private static final String TOURNAMENT_NAME = "tournamentName";
    private static final String PLAYER_ONE_FIRST_NAME = "FirstNameOne";
    private static final String PLAYER_ONE_LAST_NAME = "LastNameOne";
    private static final String PLAYER_TWO_FIRST_NAME = "FirstNameTwo";
    private static final String PLAYER_TWO_LAST_NAME = "LastNameTwo";
    private static final String PLAYER_THREE_FIRST_NAME = "FirstNameThree";
    private static final String PLAYER_THREE_LAST_NAME = "LastNameThree";

    @Mock
    private ModelMapperFactory modelMapperFactoryMock;
    @Mock
    private ModelMapper modelMapperMock;

    @Spy
    private CreateTournament createTournamentSpy;

    @InjectMocks
    private ConverterHelper testInstance;

    @Test
    public void shouldConvertCreateTournamentWithSequential() {
        TournamentDTO destination = prepareTournamentDto();

        when(modelMapperFactoryMock.getCustomizedModelMapper()).thenReturn(modelMapperMock);
        when(modelMapperMock.map(createTournamentSpy, TournamentDTO.class)).thenReturn(destination);

        TournamentDTO result = testInstance.convert(createTournamentSpy, TournamentDTO.class, true);

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result, destination),
                () -> assertTrue(result.getPlayers().stream().anyMatch(player -> player.getSequenceNumber() != null)),
                () -> assertEquals(0, result.getPlayers().get(0).getSequenceNumber()),
                () -> assertEquals(1, result.getPlayers().get(1).getSequenceNumber()),
                () -> assertEquals(2, result.getPlayers().get(2).getSequenceNumber())
        );
    }

    @Test
    public void shouldConvertCreateTournamentWithoutSequential() {
        TournamentDTO destination = prepareTournamentDto();

        when(modelMapperFactoryMock.getCustomizedModelMapper()).thenReturn(modelMapperMock);
        when(modelMapperMock.map(createTournamentSpy, TournamentDTO.class)).thenReturn(destination);

        TournamentDTO result = testInstance.convert(createTournamentSpy, TournamentDTO.class);

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result, destination),
                () -> assertTrue(result.getPlayers().stream().allMatch(player -> player.getSequenceNumber() == null))
        );
    }

    private TournamentDTO prepareTournamentDto() {
        List<PlayerDTO> player = new ArrayList<>();
        player.add(new PlayerDTO(PLAYER_ONE_FIRST_NAME, PLAYER_ONE_LAST_NAME));
        player.add(new PlayerDTO(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME));
        player.add(new PlayerDTO(PLAYER_THREE_FIRST_NAME, PLAYER_THREE_LAST_NAME));


        return TournamentDTO.builder()
                .name(TOURNAMENT_NAME)
                .type(TournamentType.ROUND)
                .players(player)
                .build();
    }


}