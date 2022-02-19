package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingleContestDtoToGetResponseConverterTest {

    private static final int PLAYER_ONE_ID = 1;
    private static final int PLAYER_TWO_ID = 2;
    private static final byte SET_ONE_PLAYER_ONE = 11;
    private static final byte SET_TWO_PLAYER_ONE = 21;
    private static final byte TIE_BREAK_PLAYER_ONE = 1;
    private static final byte SET_ONE_PLAYER_TWO = 12;
    private static final byte SET_TWO_PLAYER_TWO = 22;
    private static final byte TIE_BREAK_PLAYER_TWO = 2;

    @Spy
    private MappingContext<SingleContestDTO, GetContest> mappingContextSpy;

    SingleContestDtoToGetResponseConverter testInstance = new SingleContestDtoToGetResponseConverter();

    @Test
    public void shouldConvertContestDtoToGetResponseWithScore() {
        when(mappingContextSpy.getSource()).thenReturn(prepareContestDTO());
        when(mappingContextSpy.getDestination()).thenReturn(new GetContest());
        GetContest result = testInstance.convert(mappingContextSpy);

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result.getParticipantOne().getId(), PLAYER_ONE_ID),
                () -> assertEquals(result.getParticipantTwo().getId(), PLAYER_TWO_ID),
                () -> assertEquals(result.getSetOne().getParticipantOneScore(), SET_ONE_PLAYER_ONE),
                () -> assertEquals(result.getSetOne().getParticipantTwoScore(), SET_ONE_PLAYER_TWO),
                () -> assertEquals(result.getSetTwo().getParticipantOneScore(), SET_TWO_PLAYER_ONE),
                () -> assertEquals(result.getSetTwo().getParticipantTwoScore(), SET_TWO_PLAYER_TWO),
                () -> assertNull(result.getSetThree()),
                () -> assertEquals(result.getTieBreak().getParticipantOneScore(), TIE_BREAK_PLAYER_ONE),
                () -> assertEquals(result.getTieBreak().getParticipantTwoScore(), TIE_BREAK_PLAYER_TWO)
        );
    }

    private SingleContestDTO prepareContestDTO() {
        return SingleContestDTO.builder()
                .playerOne(PlayerDTO.builder().id(PLAYER_ONE_ID).build())
                .playerTwo(PlayerDTO.builder().id(PLAYER_TWO_ID).build())
                .setOneParticipantOne(SET_ONE_PLAYER_ONE)
                .setOneParticipantTwo(SET_ONE_PLAYER_TWO)
                .setTwoParticipantOne(SET_TWO_PLAYER_ONE)
                .setTwoParticipantTwo(SET_TWO_PLAYER_TWO)
                .setThreeParticipantOne(null)
                .setThreeParticipantTwo(null)
                .tieBreakParticipantOne(TIE_BREAK_PLAYER_ONE)
                .tieBreakParticipantTwo(TIE_BREAK_PLAYER_TWO)
                .build();
    }
}