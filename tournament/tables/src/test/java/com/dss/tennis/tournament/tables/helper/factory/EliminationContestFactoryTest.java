package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_TYPE_NOT_SUPPORTED;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EliminationContestFactoryTest {

    private static final Integer TOURNAMENT_ID = 1;

    @Spy
    private Tournament tournamentSpy;

    @InjectMocks
    private EliminationContestFactory testInstance;

    @Test
    public void shouldThrowUnsupportedErrorWhenBuildExistingTournament() {
        DetailedException result = Assertions.assertThrows(DetailedException.class, () -> testInstance
                .getContestDTOs(TOURNAMENT_ID, SingleContestDTO.class));

        Assertions.assertAll(
                () -> assertFalse(result.getErrors().isEmpty()),
                () -> assertEquals(1, result.getErrors().size()),
                () -> assertTrue(result.getErrors().stream().map(DetailedErrorData::getErrorConstant)
                        .allMatch(errorConstant -> TOURNAMENT_TYPE_NOT_SUPPORTED == errorConstant))
        );
    }

//    @Test
//    public void shouldThrowUnsupportedErrorWhenCreateContestsForNewTournament() {
//        DetailedException result = Assertions.assertThrows(DetailedException.class, () -> testInstance
//                .createContests(tournamentSpy, Lists.emptyList()));
//
//        Assertions.assertAll(
//                () -> assertFalse(result.getErrors().isEmpty()),
//                () -> assertEquals(1, result.getErrors().size()),
//                () -> assertTrue(result.getErrors().stream().map(DetailedErrorData::getErrorConstant)
//                        .allMatch(errorConstant -> TOURNAMENT_TYPE_NOT_SUPPORTED == errorConstant))
//        );
//    }
}