package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.v1.TournamentType;
import com.dss.tennis.tournament.tables.service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TournamentControllerTest {

    private static final String TOURNAMENT_NAME = "TOURNAMENT_NAME";
    private static final String PLAYER_ONE = "FirstNameOne LastNameOne";
    private static final String PLAYER_TWO = "FirstNameTwo LastNameTwo";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TournamentService tournamentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldPerformNewTournamentCreation() throws Exception {
        MvcResult content = mockMvc
                .perform(post("/tournaments")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(prepareCreateTournamentDTO())))
                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andReturn();

        verify(tournamentService).createNewTournament(any(CreateTournamentDTO.class));
        Assertions.assertEquals("", content.getResponse().getContentAsString());
    }


    private CreateTournamentDTO prepareCreateTournamentDTO() {
        return CreateTournamentDTO.builder()
                .name(TOURNAMENT_NAME)
                .players(Arrays.asList(PLAYER_ONE, PLAYER_TWO))
                .type(TournamentType.ELIMINATION)
                .build();
    }
}