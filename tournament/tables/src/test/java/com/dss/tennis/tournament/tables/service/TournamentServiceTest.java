package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.TournamentHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.CreateTournamentDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import com.dss.tennis.tournament.tables.validator.PlayerValidator;
import com.dss.tennis.tournament.tables.validator.TournamentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private static List<PlayerDTO> playerNameList;

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
    private Player playerOneEntity;
    @Spy
    private Player playerTwoEntity;
    @Spy
    private Player playerThreeEntity;
    @Spy
    private Tournament newTournament;

    @InjectMocks
    private TournamentService testInstance;

    private final PlayerDTO playerOne = new PlayerDTO(PLAYER_ONE_FIRST_NAME, PLAYER_ONE_LAST_NAME);
    private final PlayerDTO playerTwo = new PlayerDTO(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME);
    private final PlayerDTO playerThree = new PlayerDTO(PLAYER_THREE_FIRST_NAME, PLAYER_THREE_LAST_NAME);

    @BeforeEach
    void before() {
        playerNameList = Arrays.asList(playerOne, playerTwo, playerThree);
    }

    @Test
    public void shouldCreateNewTournamentWithAllowedRoundType() {
        CreateTournamentDTO createTournamentDTO = prepareRoundCreateTournamentDTO();
        when(playerRepository.findByFirstNameAndLastName(PLAYER_ONE_FIRST_NAME, PLAYER_ONE_LAST_NAME))
                .thenReturn(Optional.of(playerOneEntity));
        when(playerRepository.findByFirstNameAndLastName(PLAYER_THREE_FIRST_NAME, PLAYER_THREE_LAST_NAME))
                .thenReturn(Optional.of(playerThreeEntity));
        when(playerRepository.findByFirstNameAndLastName(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME))
                .thenReturn(Optional.empty());
        when(playerHelper.createNewPlayer(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME)).thenReturn(playerTwoEntity);
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
        verify(contestHelper).createNewContest(playerOneEntity, playerTwoEntity, newTournament);
        verify(contestHelper).createNewContest(playerOneEntity, playerThreeEntity, newTournament);
        verify(contestHelper).createNewContest(playerTwoEntity, playerThreeEntity, newTournament);
    }

    @Test
    public void shouldNotCreateNewTournamentWithNotSupportedType() {
        CreateTournamentDTO createTournamentDTO = prepareEliminationCreateTournamentDTO();

        assertThrows(DetailedException.class, () -> testInstance.createNewTournament(createTournamentDTO));
    }

    @Test
    public void shouldReturnExceptionWhenValidationFailOnCreateNewTournament() {
        DetailedErrorData detailedErrorData = new DetailedErrorData();
        when(playerValidator.validatePlayerName(playerNameList.get(0)))
                .thenReturn(Optional.of(detailedErrorData));
        CreateTournamentDTO createTournamentDTO = prepareRoundCreateTournamentDTO();

        DetailedException result = assertThrows(DetailedException.class, () -> testInstance
                .createNewTournament(createTournamentDTO));

        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().contains(detailedErrorData));
        verify(tournamentValidator).validateTournamentName(TOURNAMENT_NAME);
        playerNameList.forEach(playerName -> {
            verify(playerValidator).validatePlayerName(playerName);
        });
    }

    private CreateTournamentDTO prepareRoundCreateTournamentDTO() {
        CreateTournamentDTO createTournament = new CreateTournamentDTO();
        createTournament.setName(TOURNAMENT_NAME);
        createTournament.setType(TournamentType.ROUND);
        createTournament.setPlayers(playerNameList);

        return createTournament;
    }

    private CreateTournamentDTO prepareEliminationCreateTournamentDTO() {
        CreateTournamentDTO createTournament = new CreateTournamentDTO();
        createTournament.setName(TOURNAMENT_NAME);
        createTournament.setType(TournamentType.ELIMINATION);
        createTournament.setPlayers(playerNameList);

        return createTournament;
    }
}