package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetScore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContestDtoToGetResponseConverterTest {

    private static final int ID = 111;
    private static final byte SET_ONE_PLAYER_ONE = 11;
    private static final byte SET_TWO_PLAYER_ONE = 21;
    private static final byte TIE_BREAK_PLAYER_ONE = 1;
    private static final byte SET_ONE_PLAYER_TWO = 12;
    private static final byte SET_TWO_PLAYER_TWO = 22;
    private static final byte TIE_BREAK_PLAYER_TWO = 2;

    @Spy
    private MappingContext<ContestDTO, GetContest> mappingContextSpy;

    ContestDtoToGetResponseConverter testInstance = new ContestDtoToGetResponseConverter();

    @Test
    public void shouldConvertContestDtoToGetResponseWithScore() {
        when(mappingContextSpy.getSource()).thenReturn(prepareContestDTO());
        when(mappingContextSpy.getDestination()).thenReturn(prepareGetContest());
        GetContest result = testInstance.convert(mappingContextSpy);

        GetScore getScoreResult = result.getScore();
        Assertions.assertAll(
                () -> assertNotNull(getScoreResult),
                () -> assertEquals(getScoreResult.getSetOne().getPlayerOneScore(), SET_ONE_PLAYER_ONE),
                () -> assertEquals(getScoreResult.getSetOne().getPlayerTwoScore(), SET_ONE_PLAYER_TWO),
                () -> assertEquals(getScoreResult.getSetTwo().getPlayerOneScore(), SET_TWO_PLAYER_ONE),
                () -> assertEquals(getScoreResult.getSetTwo().getPlayerTwoScore(), SET_TWO_PLAYER_TWO),
                () -> assertNull(getScoreResult.getSetThree()),
                () -> assertEquals(getScoreResult.getTieBreak().getPlayerOneScore(), TIE_BREAK_PLAYER_ONE),
                () -> assertEquals(getScoreResult.getTieBreak().getPlayerTwoScore(), TIE_BREAK_PLAYER_TWO)
        );
    }

    private ContestDTO prepareContestDTO() {
        return ContestDTO.builder()
                .scoreId(ID)
                .setOnePlayerOne(SET_ONE_PLAYER_ONE)
                .setOnePlayerTwo(SET_ONE_PLAYER_TWO)
                .setTwoPlayerOne(SET_TWO_PLAYER_ONE)
                .setTwoPlayerTwo(SET_TWO_PLAYER_TWO)
                .setThreePlayerOne(null)
                .setThreePlayerTwo(null)
                .tieBreakPlayerOne(TIE_BREAK_PLAYER_ONE)
                .tieBreakPlayerTwo(TIE_BREAK_PLAYER_TWO)
                .build();
    }

    private GetContest prepareGetContest() {
        GetScore getScore = GetScore.builder().id(ID).build();
        return GetContest.builder().score(getScore).build();
    }
}