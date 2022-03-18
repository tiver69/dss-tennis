package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.model.db.v1.Player;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.PLAYER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerHelperTest {

    private static final Integer PLAYER_ID = 1;
    private static final String PLAYER_FIRST_NAME = "FirstNameOne";
    private static final String PLAYER_LAST_NAME = "LastNameOne";
    private static final String PLAYER_TWO_FIRST_NAME = "FirstNameTwo";
    private static final String PLAYER_TWO_LAST_NAME = "LastNameTwo";
    private static final String PLAYER_THREE_FIRST_NAME = "FirstNameThree";
    private static final String PLAYER_THREE_LAST_NAME = "LastNameThree";

    @Mock
    private PlayerRepository playerRepositoryMock;
    @Mock
    private ConverterHelper converterHelperMock;
    @Spy
    private Player playerSpy;
    @Spy
    private PlayerDTO playerDtoSpy;

    @InjectMocks
    private PlayerHelper testInstance;

    @Test
    public void shouldGetPlayerByPlayerId() {
        when(playerRepositoryMock.findById(PLAYER_ID)).thenReturn(Optional.of(playerSpy));
        when(converterHelperMock.convert(playerSpy, PlayerDTO.class)).thenReturn(playerDtoSpy);

        PlayerDTO result = testInstance.getParticipantDto(PLAYER_ID);

        Assertions.assertEquals(playerDtoSpy, result);
    }

    @Test
    public void shouldNotGetPlayerByPlayerId() {
        when(playerRepositoryMock.findById(PLAYER_ID)).thenReturn(Optional.empty());

        DetailedException result = Assertions.assertThrows(DetailedException.class, () -> {
            testInstance.getParticipant(PLAYER_ID);
        });

        Assertions.assertAll(
                () -> assertFalse(result.getErrors().isEmpty()),
                () -> assertEquals(1, result.getErrors().size()),
                () -> assertTrue(result.getErrors().stream().map(DetailedErrorData::getErrorConstant)
                        .allMatch(errorConstant -> PLAYER_NOT_FOUND == errorConstant))
        );
    }

//    @Test
//    public void shouldGetPlayerByPlayerDTO() {
//        when(playerRepositoryMock.findByFirstNameAndLastName(PLAYER_FIRST_NAME, PLAYER_LAST_NAME))
//                .thenReturn(Optional.of(playerSpy));
//
//        Player result = testInstance.getPlayer(new PlayerDTO(PLAYER_FIRST_NAME, PLAYER_LAST_NAME));
//
//        Assertions.assertEquals(playerSpy, result);
//    }
//
//    @Test
//    public void shouldThrowErrorWhenPlayerNotFoundByPlayerDTO() {
//        when(playerRepositoryMock.findByFirstNameAndLastName(PLAYER_FIRST_NAME, PLAYER_LAST_NAME))
//                .thenReturn(Optional.empty());
//
//        DetailedException result = Assertions.assertThrows(DetailedException.class, () -> {
//            testInstance.getPlayer(new PlayerDTO(PLAYER_FIRST_NAME, PLAYER_LAST_NAME));
//        });
//        Assertions.assertAll(
//                () -> assertFalse(result.getErrors().isEmpty()),
//                () -> assertEquals(1, result.getErrors().size()),
//                () -> assertTrue(result.getErrors().stream().map(DetailedErrorData::getErrorConstant)
//                        .allMatch(errorConstant -> PLAYER_NOT_FOUND_TOURNAMENT_CREATION == errorConstant))
//        );
//    }

    @Test
    public void shouldCheckIfPlayerExistsWithBothNameProvided() {
        when(playerRepositoryMock.findByFirstNameAndLastName(PLAYER_FIRST_NAME, PLAYER_LAST_NAME))
                .thenReturn(Optional.of(playerSpy));

        Assertions.assertTrue(testInstance.isPlayerExist(new PlayerDTO(PLAYER_FIRST_NAME, PLAYER_LAST_NAME)));
    }

//    @Test
//    public void shouldCheckIfPlayerNotExistsWithBothNameProvided() {
//        when(playerRepositoryMock.findByFirstNameAndLastName(PLAYER_FIRST_NAME, PLAYER_LAST_NAME))
//                .thenReturn(Optional.of(playerSpy));
//
//        Assertions.assertFalse(testInstance.isPlayerNotExist(new PlayerDTO(PLAYER_FIRST_NAME, PLAYER_LAST_NAME)));
//    }

//    @ParameterizedTest
//    @MethodSource("withoutBothNameProvided")
//    public void shouldCheckIfPlayerExistsWithoutBothNameProvided(String firstName, String lastName) {
//        Assertions.assertFalse(testInstance.isPlayerExist(new PlayerDTO(firstName, lastName)));
//    }

//    @ParameterizedTest
//    @MethodSource("withoutBothNameProvided")
//    public void shouldCheckIfPlayerNotExistsWithoutBothNameProvided(String firstName, String lastName) {
//        Assertions.assertTrue(testInstance.isPlayerNotExist(new PlayerDTO(firstName, lastName)));
//    }

    private static Stream<Arguments> withoutBothNameProvided() {
        return Stream.of(
                Arguments.of(null, null, false),
                Arguments.of("", "", false),
                Arguments.of(PLAYER_FIRST_NAME, "", false),
                Arguments.of(null, PLAYER_LAST_NAME, false)
        );
    }

    @Test
    public void shouldCreateNewPlayer() {
        testInstance.createNewPlayer(PLAYER_FIRST_NAME, PLAYER_LAST_NAME);

        verify(playerRepositoryMock).save(any(Player.class));
    }

//    @Test
//    public void shouldRemoveAllPlayerDuplicates() {
//        List<PlayerDTO> players = preparePlayers();
//        List<PlayerDTO> result = testInstance.removePlayerDuplicates(players);
//
//        Assertions.assertAll(
//                () -> assertEquals(2, result.size()),
//                () -> assertEquals(3, players.size()),
//                () -> assertEquals(PLAYER_FIRST_NAME, result.get(0).getFirstName()),
//                () -> assertEquals(PLAYER_TWO_FIRST_NAME, result.get(1).getFirstName())
//        );
//    }

    private List<PlayerDTO> preparePlayers() {
        List<PlayerDTO> players = new ArrayList<>();
        players.add(
                (new PlayerDTO(PLAYER_FIRST_NAME, PLAYER_LAST_NAME)));
        players.add(new PlayerDTO(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME));
        players.add(new PlayerDTO(PLAYER_FIRST_NAME, PLAYER_LAST_NAME));
        players.add(new PlayerDTO(PLAYER_TWO_FIRST_NAME, PLAYER_TWO_LAST_NAME));
        players.add(new PlayerDTO(PLAYER_THREE_FIRST_NAME, PLAYER_THREE_LAST_NAME));
        return players;
    }
}