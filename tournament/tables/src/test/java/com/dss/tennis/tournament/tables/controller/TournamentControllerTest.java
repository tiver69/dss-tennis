package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.request.CreatePlayer;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import com.dss.tennis.tournament.tables.service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TournamentControllerTest {

    private static final String TOURNAMENT_NAME = "TOURNAMENT_NAME";
    private static final CreatePlayer PLAYER_ONE = new CreatePlayer();
    private static final CreatePlayer PLAYER_TWO = new CreatePlayer();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TournamentService tournamentService;
    @MockBean
    private ModelMapper modelMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldPerformNewTournamentCreation() throws Exception {
        when(modelMapper.map(any(CreateTournament.class), eq(CreateTournamentDTO.class)))
                .thenReturn(new CreateTournamentDTO());

        MvcResult content = mockMvc
                .perform(post("/tournaments")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(prepareCreateTournament())))
                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andReturn();

        verify(modelMapper).map(any(CreateTournament.class), eq(CreateTournamentDTO.class));
        verify(tournamentService).createNewTournament(any(CreateTournamentDTO.class));
        Assertions.assertEquals("", content.getResponse().getContentAsString());
    }


    private CreateTournament prepareCreateTournament() {
        return CreateTournament.builder()
                .name(TOURNAMENT_NAME)
                .players(Arrays.asList(PLAYER_ONE, PLAYER_TWO))
                .type(TournamentType.ELIMINATION)
                .build();
    }
}