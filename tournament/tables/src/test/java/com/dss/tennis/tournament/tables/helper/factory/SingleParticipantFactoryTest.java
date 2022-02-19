package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingleParticipantFactoryTest {

    private static final Integer TOURNAMENT_ID = 1;

    @Mock
    private PlayerRepository playerRepositoryMock;
    @Mock
    private ConverterHelper converterHelperMock;
    @Spy
    private PlayerDTO playerOneDtoSpy;
    @Spy
    private Player playerOneSpy;
    @Spy
    private PlayerDTO playerTwoDtoSpy;
    @Spy
    private Player playerTwoSpy;

    @InjectMocks
    private SingleParticipantFactory testInstance;

    @Test
    public void shouldCreateNewContestsForRoundTournament() {
        when(playerRepositoryMock.findPlayersBySingleTournamentId(TOURNAMENT_ID))
                .thenReturn(Lists.newArrayList(playerOneSpy, playerTwoSpy));
        when(converterHelperMock.convert(playerOneSpy, PlayerDTO.class)).thenReturn(playerOneDtoSpy);
        when(converterHelperMock.convert(playerTwoSpy, PlayerDTO.class)).thenReturn(playerTwoDtoSpy);

        List<PlayerDTO> result = testInstance.getTournamentPlayers(TOURNAMENT_ID);
        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.contains(playerOneDtoSpy)),
                () -> assertTrue(result.contains(playerTwoDtoSpy))
        );
    }
}