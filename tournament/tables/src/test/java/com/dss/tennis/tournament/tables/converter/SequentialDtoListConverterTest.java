package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.request.CreatePlayer;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequentialDtoListConverterTest {

    private static final String TOURNAMENT_NAME = "tournamentName";
    private static final String PLAYER_ONE_FIRST_NAME = "FirstNameOne";
    private static final String PLAYER_ONE_LAST_NAME = "LastNameOne";
    private static final String PLAYER_TWO_FIRST_NAME = "FirstNameTwo";
    private static final String PLAYER_TWO_LAST_NAME = "LastNameTwo";
    private static final String PLAYER_THREE_FIRST_NAME = "FirstNameThree";
    private static final String PLAYER_THREE_LAST_NAME = "LastNameThree";

    private static final List<CreatePlayer> PLAYERS = new ArrayList<>();
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    @BeforeAll
    static void before() {
        MODEL_MAPPER.addConverter(new SequentialDtoListConverter());

        PLAYERS.add(new CreatePlayer(PLAYER_ONE_FIRST_NAME, PLAYER_ONE_LAST_NAME));
        PLAYERS.add(new CreatePlayer(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME));
        PLAYERS.add(new CreatePlayer(PLAYER_THREE_FIRST_NAME, PLAYER_THREE_LAST_NAME));
    }

    @Test
    public void shouldConvertToCreateTournamentDTOWithSequenceNumbers() {
        TournamentDTO result = MODEL_MAPPER
                .map(CreateTournament.builder().name(TOURNAMENT_NAME).type(TournamentType.ROUND)
                        .players(PLAYERS).build(), TournamentDTO.class);

        Assertions.assertAll(
                () -> assertEquals(result.getName(), TOURNAMENT_NAME),
                () -> assertEquals(result.getType(), TournamentType.ROUND),
                () -> assertEquals(result.getPlayers().size(), PLAYERS.size()),
                () -> assertEquals(result.getPlayers().get(0).getSequenceNumber(), 0),
                () -> assertEquals(result.getPlayers().get(1).getSequenceNumber(), 1),
                () -> assertEquals(result.getPlayers().get(2).getSequenceNumber(), 2),
                () -> assertEquals(result.getPlayers().get(2).getFirstName(), PLAYER_THREE_FIRST_NAME),
                () -> assertEquals(result.getPlayers().get(2).getLastName(), PLAYER_THREE_LAST_NAME)
        );
    }
}