package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.RequestParameterHelper;
import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.dto.SuccessResponseDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.request.CreatePlayer;
import com.dss.tennis.tournament.tables.model.request.CreateTournament;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import com.dss.tennis.tournament.tables.model.response.v1.SuccessResponse;
import com.dss.tennis.tournament.tables.service.TournamentService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;

import static com.dss.tennis.tournament.tables.helper.RequestParameterHelper.INCLUDE_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TournamentControllerTest {

    private static final int TOURNAMENT_ID = 111;
    private static final String TOURNAMENT_NAME = "TOURNAMENT_NAME";
    private static final String WARNING_CODE = "WARNING_CODE";
    private static final String INCLUDE_PARAMETER = "INCLUDE_PARAMETER";
    private static final CreatePlayer PLAYER_ONE = new CreatePlayer();
    private static final CreatePlayer PLAYER_TWO = new CreatePlayer();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TournamentService tournamentServiceMock;
    @MockBean
    private ConverterHelper converterHelperMock;
    @MockBean
    private ResponseHelper responseHelperMock;
    @MockBean
    private RequestParameterHelper requestParameterHelperMock;

    @Spy
    private TournamentDTO tournamentDtoSpy;
    @Spy
    private SuccessResponseDTO<TournamentDTO> createdTournamentDtoSpy;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    @Test
    public void shouldPerformNewTournamentCreation() throws Exception {
        SuccessResponse<GetTournament> response = prepareSuccessGetTournament(List
                .of(ErrorData.builder().code(WARNING_CODE).build()));
        when(converterHelperMock.convert(any(CreateTournament.class), eq(TournamentDTO.class), eq(true)))
                .thenReturn(tournamentDtoSpy);
        when(tournamentServiceMock.createNewTournament(tournamentDtoSpy)).thenReturn(createdTournamentDtoSpy);
        when(responseHelperMock.createSuccessResponse(createdTournamentDtoSpy, GetTournament.class))
                .thenReturn(response);

        MvcResult content = mockMvc
                .perform(post("/tournaments")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(prepareCreateTournament())))
                .andExpect(status().isCreated())
//                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andReturn();

        verify(converterHelperMock).convert(any(CreateTournament.class), eq(TournamentDTO.class), eq(true));
        verify(tournamentServiceMock).createNewTournament(any(TournamentDTO.class));
        verify(responseHelperMock).createSuccessResponse(createdTournamentDtoSpy, GetTournament.class);
        Assertions.assertEquals(objectMapper.writeValueAsString(response), content.getResponse()
                .getContentAsString());
    }

    @Test
    public void shouldPerformGetTournamentWithIncorrectIncludeParameter() throws Exception {
        List<ErrorData> includeWarnings = List.of(ErrorData.builder().code(WARNING_CODE).build());
        when(requestParameterHelperMock
                .populateRequestParameter(eq(INCLUDE_KEY), eq(INCLUDE_PARAMETER), any(RequestParameter.class)))
                .thenReturn(includeWarnings);
        when(tournamentServiceMock.getTournament(eq(TOURNAMENT_ID), any(RequestParameter.class)))
                .thenReturn(tournamentDtoSpy);
        when(responseHelperMock.createSuccessResponse(tournamentDtoSpy, includeWarnings))
                .thenReturn(prepareSuccessGetTournament(includeWarnings));

        MvcResult content = mockMvc
                .perform(get("/tournaments/" + TOURNAMENT_ID + "?include=" + INCLUDE_PARAMETER))
                .andExpect(status().isOk())
                .andReturn();

        verify(tournamentServiceMock).getTournament(eq(TOURNAMENT_ID), any(RequestParameter.class));
        verify(responseHelperMock).createSuccessResponse(any(TournamentDTO.class), eq(includeWarnings));
        Assertions.assertEquals(objectMapper.writeValueAsString(prepareSuccessGetTournament(includeWarnings)), content
                .getResponse().getContentAsString());
    }

    private SuccessResponse<GetTournament> prepareSuccessGetTournament(List<ErrorData> warnings) {
        return new SuccessResponse<>(prepareGetTournament(), warnings);
    }

    private GetTournament prepareGetTournament() {
        return GetTournament.builder()
                .name(TOURNAMENT_NAME)
                .tournamentType(TournamentType.ELIMINATION)
                .build();
    }

    private CreateTournament prepareCreateTournament() {
        return CreateTournament.builder()
                .name(TOURNAMENT_NAME)
                .players(Arrays.asList(PLAYER_ONE, PLAYER_TWO))
                .tournamentType(TournamentType.ELIMINATION)
                .build();
    }
}