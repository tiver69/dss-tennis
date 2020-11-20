package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.model.v1.Player;
import com.dss.tennis.tournament.tables.model.v1.Tournament;
import com.dss.tennis.tournament.tables.model.v1.TournamentType;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    private static final String TOURNAMENT_NAME = "TOURNAMENT_NAME";
    private static final String PLAYER_ONE_FIRST_NAME = "FirstNameOne";
    private static final String PLAYER_ONE_LAST_NAME = "LastNameOne";
    private static final String PLAYER_TWO_FIRST_NAME = "FirstNameTwo";
    private static final String PLAYER_TWO_LAST_NAME = "LastNameTwo";
    private static final String PLAYER_THREE_FIRST_NAME = "FirstNameThree";
    private static final String PLAYER_THREE_LAST_NAME = "LastNameThree";

    private static List<String> playerNameList;

    @Mock
    private TournamentValidator tournamentValidator;
    @Mock
    private PlayerValidator playerValidator;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerHelper playerHelper;
    @Mock
    private TournamentHelper tournamentHelper;
    @Mock
    private ContestHelper contestHelper;
    @Spy
    private Player playerOne;
    @Spy
    private Player playerTwo;
    @Spy
    private Player playerThree;
    @Spy
    private Tournament newTournament;

    @InjectMocks
    TournamentService testInstance;

    @BeforeAll
    static void beforeAll() {
        playerNameList = Arrays.asList(PLAYER_ONE_FIRST_NAME + " " + PLAYER_ONE_LAST_NAME,
                PLAYER_TWO_FIRST_NAME + " " + PLAYER_TWO_LAST_NAME,
                PLAYER_THREE_FIRST_NAME + " " + PLAYER_THREE_LAST_NAME);
    }

    @Test
    public void shouldCreateNewTournament() {
        CreateTournamentDTO createTournamentDTO = prepareEliminationCreateTournamentDTO();
        when(playerRepository.findByFirstNameAndLastName(PLAYER_ONE_FIRST_NAME, PLAYER_ONE_LAST_NAME))
                .thenReturn(Optional.of(playerOne));
        when(playerRepository.findByFirstNameAndLastName(PLAYER_THREE_FIRST_NAME, PLAYER_THREE_LAST_NAME))
                .thenReturn(Optional.of(playerThree));
        when(playerRepository.findByFirstNameAndLastName(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME))
                .thenReturn(Optional.empty());
        when(playerHelper.createNewPlayer(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME)).thenReturn(playerTwo);
        when(tournamentHelper.createNewTournament(createTournamentDTO)).thenReturn(newTournament);

        testInstance.createNewTournament(createTournamentDTO);

        verify(tournamentValidator).validateTournamentName(TOURNAMENT_NAME);
        playerNameList.forEach(playerName -> {
            verify(playerValidator).validatePlayerName(playerName);
        });
        verify(playerRepository).findByFirstNameAndLastName(PLAYER_ONE_FIRST_NAME, PLAYER_ONE_LAST_NAME);
        verify(playerRepository).findByFirstNameAndLastName(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME);
        verify(playerRepository).findByFirstNameAndLastName(PLAYER_THREE_FIRST_NAME, PLAYER_THREE_LAST_NAME);

        verify(tournamentHelper).createNewTournament(createTournamentDTO);
        verify(playerHelper).createNewPlayer(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME);
        verify(contestHelper).createNewContest(playerOne, playerTwo, newTournament);
        verify(contestHelper).createNewContest(playerOne, playerThree, newTournament);
        verify(contestHelper).createNewContest(playerTwo, playerThree, newTournament);
    }

    @Test
    public void shouldReturnExceptionWhenValidationFailOnCreateNewTournament() {
        when(playerValidator.validatePlayerName(playerNameList.get(0))).thenReturn(Optional.of(anyString()));
        CreateTournamentDTO createTournamentDTO = prepareEliminationCreateTournamentDTO();

        assertThrows(IllegalArgumentException.class, () -> testInstance.createNewTournament(createTournamentDTO));

        verify(tournamentValidator).validateTournamentName(TOURNAMENT_NAME);
        playerNameList.forEach(playerName -> {
            verify(playerValidator).validatePlayerName(playerName);
        });
    }


    private CreateTournamentDTO prepareEliminationCreateTournamentDTO() {
        CreateTournamentDTO createTournament = new CreateTournamentDTO();
        createTournament.setName(TOURNAMENT_NAME);
        createTournament.setType(TournamentType.ELIMINATION);
        createTournament.setPlayers(playerNameList);

        return createTournament;
    }
}