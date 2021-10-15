package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.request.CreatePlayer;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import com.dss.tennis.tournament.tables.service.TournamentService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TournamentControllerTest {

    private static final int TOURNAMENT_ID = 111;
    private static final String TOURNAMENT_NAME = "TOURNAMENT_NAME";
    private static final CreatePlayer PLAYER_ONE = new CreatePlayer();
    private static final CreatePlayer PLAYER_TWO = new CreatePlayer();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TournamentService tournamentServiceMock;
    @MockBean
    private ConverterHelper converterHelperMock;

    @Spy
    private TournamentDTO tournamentDtoSpy;
    @Spy
    private TournamentDTO createdTournamentDtoSpy;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    @Test
    public void shouldPerformNewTournamentCreation() throws Exception {
        when(converterHelperMock.convert(any(CreateTournament.class), eq(TournamentDTO.class), eq(true)))
                .thenReturn(tournamentDtoSpy);
        when(tournamentServiceMock.createNewTournament(tournamentDtoSpy)).thenReturn(createdTournamentDtoSpy);
        when(converterHelperMock.convert(createdTournamentDtoSpy, GetTournament.class))
                .thenReturn(prepareGetTournament());

        MvcResult content = mockMvc
                .perform(post("/tournaments")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(prepareCreateTournament())))
                .andExpect(status().isCreated())
//                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andReturn();

        verify(converterHelperMock).convert(any(CreateTournament.class), eq(TournamentDTO.class), eq(true));
        verify(tournamentServiceMock).createNewTournament(any(TournamentDTO.class));
        verify(converterHelperMock).convert(any(TournamentDTO.class), eq(GetTournament.class));
        Assertions.assertEquals(objectMapper.writeValueAsString(prepareGetTournament()), content.getResponse()
                .getContentAsString());
    }

    @Test
    public void shouldPerformGetTournament() throws Exception {
        when(tournamentServiceMock.getTournament(TOURNAMENT_ID)).thenReturn(tournamentDtoSpy);
        when(converterHelperMock.convert(tournamentDtoSpy, GetTournament.class))
                .thenReturn(prepareGetTournament());

        MvcResult content = mockMvc
                .perform(get("/tournaments/" + TOURNAMENT_ID))
                .andExpect(status().isOk())
                .andReturn();

        verify(tournamentServiceMock).getTournament(TOURNAMENT_ID);
        verify(converterHelperMock).convert(any(TournamentDTO.class), eq(GetTournament.class));
        Assertions.assertEquals(objectMapper.writeValueAsString(prepareGetTournament()), content.getResponse()
                .getContentAsString());
    }

    private GetTournament prepareGetTournament() {
        return GetTournament.builder()
                .name(TOURNAMENT_NAME)
                .type(TournamentType.ELIMINATION)
                .build();
    }

    private CreateTournament prepareCreateTournament() {
        return CreateTournament.builder()
                .name(TOURNAMENT_NAME)
                .players(Arrays.asList(PLAYER_ONE, PLAYER_TWO))
                .type(TournamentType.ELIMINATION)
                .build();
    }
}