package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_HAS_NO_CONTESTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContestHelperTest {

    private static final int TOURNAMENT_ID = 1;

    @Mock
    private ContestRepository contestRepositoryMock;

    @Spy
    private Player playerOneSpy;
    @Spy
    private Player playerTwoSpy;
    @Spy
    private SingleContest singleContestOneSpy;
    @Spy
    private SingleContest singleContestTwoSpy;
    @Spy
    private Tournament tournamentSpy;

    @InjectMocks
    private ContestHelper testInstance;

    @Test
    public void shouldThrowExceptionWhenTournamentContestsAreEmpty() {
        when(contestRepositoryMock.findByTournamentId(TOURNAMENT_ID))
                .thenReturn(Collections.emptyList());

        DetailedException resultError = Assertions
                .assertThrows(DetailedException.class, () -> testInstance.getTournamentContests(TOURNAMENT_ID));
        Set<DetailedErrorData> result = resultError.getErrors();

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream().map(DetailedErrorData::getErrorConstant)
                        .anyMatch(tt -> tt.equals(TOURNAMENT_HAS_NO_CONTESTS)))
        );
    }

    @Test
    public void shouldGetTournamentContests() {
        when(contestRepositoryMock.findByTournamentId(TOURNAMENT_ID))
                .thenReturn(Arrays.asList(singleContestOneSpy, singleContestTwoSpy));

        List<Contest> result = testInstance.getTournamentContests(TOURNAMENT_ID);

        Assertions.assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.containsAll(Arrays.asList(singleContestOneSpy, singleContestTwoSpy)))
        );
    }

    @Test
    public void shouldCreateNewContest() {
        testInstance.createNewContest(playerOneSpy, playerTwoSpy, tournamentSpy);

        verify(contestRepositoryMock).save(any(SingleContest.class));
    }
}