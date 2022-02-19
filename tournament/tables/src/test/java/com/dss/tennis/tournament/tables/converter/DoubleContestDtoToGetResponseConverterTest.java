package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetDoubleParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoubleContestDtoToGetResponseConverterTest {

    private static final int TEAM_ONE_ID = 12;
    private static final int TEAM_TWO_ID = 34;
    private static final int TEAM_ONE_PLAYER_ONE_ID = 1;
    private static final int TEAM_ONE_PLAYER_TWO_ID = 2;
    private static final int TEAM_TWO_PLAYER_ONE_ID = 3;
    private static final int TEAM_TWO_PLAYER_TWO_ID = 4;
    private static final byte SET_ONE_PLAYER_ONE = 11;
    private static final byte SET_TWO_PLAYER_ONE = 21;
    private static final byte TIE_BREAK_PLAYER_ONE = 1;
    private static final byte SET_ONE_PLAYER_TWO = 12;
    private static final byte SET_TWO_PLAYER_TWO = 22;
    private static final byte TIE_BREAK_PLAYER_TWO = 2;

    @Spy
    private MappingContext<DoubleContestDTO, GetContest> mappingContextSpy;

    DoubleContestDtoToGetResponseConverter testInstance = new DoubleContestDtoToGetResponseConverter();

    @Test
    public void shouldConvertContestDtoToGetResponseWithScore() {
        when(mappingContextSpy.getSource()).thenReturn(prepareContestDTO());
        when(mappingContextSpy.getDestination()).thenReturn(new GetContest());

        GetContest result = testInstance.convert(mappingContextSpy);
        GetDoubleParticipant participantOne = (GetDoubleParticipant) result.getParticipantOne();
        GetDoubleParticipant participantTwo = (GetDoubleParticipant) result.getParticipantTwo();

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(participantOne.getId(), TEAM_ONE_ID),
                () -> assertEquals(participantTwo.getId(), TEAM_TWO_ID),
                () -> assertEquals(participantOne.getPlayerOneId(), TEAM_ONE_PLAYER_ONE_ID),
                () -> assertEquals(participantOne.getPlayerTwoId(), TEAM_ONE_PLAYER_TWO_ID),
                () -> assertEquals(participantTwo.getPlayerOneId(), TEAM_TWO_PLAYER_ONE_ID),
                () -> assertEquals(participantTwo.getPlayerTwoId(), TEAM_TWO_PLAYER_TWO_ID),
                () -> assertEquals(result.getSetOne().getParticipantOneScore(), SET_ONE_PLAYER_ONE),
                () -> assertEquals(result.getSetOne().getParticipantTwoScore(), SET_ONE_PLAYER_TWO),
                () -> assertEquals(result.getSetTwo().getParticipantOneScore(), SET_TWO_PLAYER_ONE),
                () -> assertEquals(result.getSetTwo().getParticipantTwoScore(), SET_TWO_PLAYER_TWO),
                () -> assertNull(result.getSetThree()),
                () -> assertEquals(result.getTieBreak().getParticipantOneScore(), TIE_BREAK_PLAYER_ONE),
                () -> assertEquals(result.getTieBreak().getParticipantTwoScore(), TIE_BREAK_PLAYER_TWO)
        );
    }

    private DoubleContestDTO prepareContestDTO() {
        return DoubleContestDTO.builder()
                .teamOne(TeamDTO.builder().id(TEAM_ONE_ID).playerOne(new PlayerDTO(TEAM_ONE_PLAYER_ONE_ID))
                        .playerTwo(new PlayerDTO(TEAM_ONE_PLAYER_TWO_ID)).build())
                .teamTwo(TeamDTO.builder().id(TEAM_TWO_ID).playerOne(new PlayerDTO(TEAM_TWO_PLAYER_ONE_ID))
                        .playerTwo(new PlayerDTO(TEAM_TWO_PLAYER_TWO_ID)).build())
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