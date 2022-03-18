package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    private static final int PLAYER_ID = 111;

    @Mock
    private PlayerHelper playerHelperMock;
    @Spy
    private PlayerDTO playerDtoSpy;

    @InjectMocks
    private ParticipantService testInstance;

    @Test
    public void shouldGetPlayer() {
        when(playerHelperMock.getParticipantDto(PLAYER_ID)).thenReturn(playerDtoSpy);

        Assertions.assertEquals(playerDtoSpy, testInstance.getPlayerDTO(PLAYER_ID));
    }

}