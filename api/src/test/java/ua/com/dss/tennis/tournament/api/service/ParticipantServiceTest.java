package ua.com.dss.tennis.tournament.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.dss.tennis.tournament.api.helper.participant.PlayerHelper;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;

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