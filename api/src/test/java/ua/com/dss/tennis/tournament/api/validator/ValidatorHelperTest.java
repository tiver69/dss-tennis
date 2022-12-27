package ua.com.dss.tennis.tournament.api.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.TOURNAMENT_NAME_EMPTY;

@ExtendWith(MockitoExtension.class)
class ValidatorHelperTest {

    private static final String VALID_FIRST_NAME = "FirstName";
    private static final String VALID_LAST_NAME = "LastName  ";
    private static final byte SEQUENCE_NUMBER = 1;

    @Mock
    private Validator javaxValidatorMock;
    @Mock
    private ConstraintViolation constraintViolationMock;

    @InjectMocks
    ValidatorHelper testInstance;

    @Test
    public void shouldPassValidation() {
        PlayerDTO playerDTO = new PlayerDTO(VALID_FIRST_NAME, VALID_LAST_NAME);
        when(javaxValidatorMock.validate(playerDTO)).thenReturn(Sets.newSet());

        Set<ErrorDataDTO> result = testInstance.validateObject(playerDTO);

        assertTrue(result.isEmpty());
    }

//    @Test
//    public void shouldReturnDetailedErrorDataWithSequentialNumber() {
//        PlayerDTO playerDTO = new PlayerDTO("", VALID_LAST_NAME);
//        playerDTO.setSequenceNumber(SEQUENCE_NUMBER);
//
//        when(constraintViolationMock.getMessage()).thenReturn(PLAYER_FIRST_NAME_EMPTY.name());
//        when(javaxValidatorMock.validate(playerDTO)).thenReturn(Sets.newSet(constraintViolationMock));
//
//        Set<DetailedErrorData> result = testInstance.validateObject(playerDTO);
//
//        Assertions.assertAll(
//                () -> assertFalse(result.isEmpty()),
//                () -> assertEquals(1, result.size()),
//                () -> assertTrue(result.stream()
//                        .anyMatch(tt -> tt.getErrorConstant().equals(PLAYER_FIRST_NAME_EMPTY))),
//                () -> assertTrue(result.stream()
//                        .anyMatch(tt -> tt.getSequentNumber() == SEQUENCE_NUMBER))
//        );
//    }

    @Test
    public void shouldReturnDetailedErrorDataWithoutSequentialNumber() {
        TournamentDTO tournamentDTO = new TournamentDTO();
        when(constraintViolationMock.getMessage()).thenReturn(TOURNAMENT_NAME_EMPTY.name());
        when(javaxValidatorMock.validate(tournamentDTO)).thenReturn(Sets.newSet(constraintViolationMock));

        Set<ErrorDataDTO> result = testInstance.validateObject(tournamentDTO);

        Assertions.assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.stream()
                        .anyMatch(tt -> tt.getErrorKey().equals(TOURNAMENT_NAME_EMPTY))),
                () -> assertFalse(result.stream()
                        .anyMatch(tt -> tt.getSequentNumber() != null))
        );
    }
}
