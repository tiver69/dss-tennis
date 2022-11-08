package com.dss.tennis.tournament.tables.controller;

import com.dss.tennis.tournament.tables.helper.ResponseHelper;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetPlayer;
import com.dss.tennis.tournament.tables.model.response.v1.SuccessResponse;
import com.dss.tennis.tournament.tables.service.ParticipantService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ParticipantControllerTest {

    private static final int PLAYER_ID = 111;
    private static final String PLAYER_FIRST_NAME = "FIRST_NAME";
    private static final String PLAYER_LAST_NAME = "LAST_NAME";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ParticipantService participantServiceMock;
    @MockBean
    private ResponseHelper responseHelperMock;

    @Spy
    private PlayerDTO playerDtoSpy;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

//    @Test
//    public void shouldPerformGetPlayer() throws Exception {
//        SuccessResponse<GetPlayer> successResponse = prepareSuccessGetPlayer();
//        when(participantServiceMock.getPlayerDTO(PLAYER_ID)).thenReturn(playerDtoSpy);
//        when(responseHelperMock.createSuccessResponse(playerDtoSpy, GetPlayer.class))
//                .thenReturn(successResponse);
//
//        MvcResult content = mockMvc
//                .perform(get("/participants/players/" + PLAYER_ID))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        verify(participantServiceMock).getPlayerDTO(PLAYER_ID);
//        verify(responseHelperMock).createSuccessResponse(playerDtoSpy, GetPlayer.class);
//        Assertions.assertEquals(objectMapper.writeValueAsString(successResponse), content
//                .getResponse().getContentAsString());
//    }

    private SuccessResponse<GetPlayer> prepareSuccessGetPlayer() {
        GetPlayer player = GetPlayer.builder()
                .id(PLAYER_ID)
                .firstName(PLAYER_FIRST_NAME)
                .lastName(PLAYER_LAST_NAME)
                .build();
        return new SuccessResponse<>(player, null);
    }
}