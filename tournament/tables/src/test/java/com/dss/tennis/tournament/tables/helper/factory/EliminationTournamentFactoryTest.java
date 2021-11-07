package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_TYPE_NOT_SUPPORTED;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EliminationTournamentFactoryTest {

    @Spy
    private Tournament tournamentSpy;
    @Spy
    private TournamentDTO tournamentDtoSpy;

    @InjectMocks
    private EliminationTournamentFactory testInstance;

    @Test
    public void shouldThrowUnsupportedErrorWhenBuildExistingTournament() {
        DetailedException result = Assertions.assertThrows(DetailedException.class, () -> testInstance
                .buildExistingTournament(tournamentDtoSpy));

        Assertions.assertAll(
                () -> assertFalse(result.getErrors().isEmpty()),
                () -> assertEquals(1, result.getErrors().size()),
                () -> assertTrue(result.getErrors().stream().map(DetailedErrorData::getErrorConstant)
                        .allMatch(errorConstant -> TOURNAMENT_TYPE_NOT_SUPPORTED == errorConstant))
        );
    }

    @Test
    public void shouldThrowUnsupportedErrorWhenCreateContestsForNewTournament() {
        DetailedException result = Assertions.assertThrows(DetailedException.class, () -> testInstance
                .createNewContests(tournamentSpy, Lists.emptyList()));

        Assertions.assertAll(
                () -> assertFalse(result.getErrors().isEmpty()),
                () -> assertEquals(1, result.getErrors().size()),
                () -> assertTrue(result.getErrors().stream().map(DetailedErrorData::getErrorConstant)
                        .allMatch(errorConstant -> TOURNAMENT_TYPE_NOT_SUPPORTED == errorConstant))
        );
    }
}