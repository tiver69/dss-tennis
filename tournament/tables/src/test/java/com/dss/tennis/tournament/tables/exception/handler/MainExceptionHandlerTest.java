package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
import com.dss.tennis.tournament.tables.exception.handler.MainExceptionHandler.ErrorResponse;
import com.dss.tennis.tournament.tables.model.db.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.db.v1.ErrorDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.PLAYER_FIRST_NAME_EMPTY;
import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.TOURNAMENT_NAME_EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainExceptionHandlerTest {

    private static final ErrorConstants TEST_CONSTANT = ErrorConstants.TOURNAMENT_NAME_DUPLICATE;
    private static final String ANY_DETAIL = "Detail";
    private static final String ERROR_DETAIL_DATA = "DetailData";
    private static final String ERROR_DETAIL_DATA_2 = "DetailData2";
    private static final String CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private static final String POINTER_SUFFIX = ".pointer";
    private static final String SEQUENTIAL_POINTER_FORMAT = "[%o]";
    private static final String SEQUENTIAL_POINTER_RESPONSE = "[1]";
    private static final Integer SEQUENCE_NUMBER = 1;

    @Mock
    private Environment environmentMock;

    @InjectMocks
    private MainExceptionHandler testInstance;

    @BeforeEach
    void setUp() {
        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);
        when(environmentMock.getProperty(TEST_CONSTANT + testInstance.CODE_SUFFIX)).thenReturn(CODE);
    }

    @Test
    public void shouldHandleErrorWithSingleConstant() {
        ResponseEntity<ErrorResponse> result =
                testInstance.handleDetailedException(new DetailedException(TEST_CONSTANT, ERROR_DETAIL_DATA));
        List<ErrorData> resultResponseErrors = result.getBody().getErrors();

        verify(environmentMock, times(5)).getProperty(any(String.class));
        Assertions.assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> assertEquals(1, resultResponseErrors.size()),
                () -> assertEquals(ERROR_DETAIL_DATA, resultResponseErrors.get(0).getSource().getParameter())
        );
    }

    @Test
    public void shouldHandleErrorWithSingleSequentialConstant() {
        when(environmentMock.getProperty(TEST_CONSTANT + POINTER_SUFFIX)).thenReturn(SEQUENTIAL_POINTER_FORMAT);

        ResponseEntity<ErrorResponse> result =
                testInstance
                        .handleDetailedException(new DetailedException(TEST_CONSTANT, ERROR_DETAIL_DATA,
                                SEQUENCE_NUMBER));
        List<ErrorData> resultResponseErrors = result.getBody().getErrors();

        verify(environmentMock, times(5)).getProperty(any(String.class));
        Assertions.assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> assertEquals(1, resultResponseErrors.size()),
                () -> assertEquals(ERROR_DETAIL_DATA, resultResponseErrors.get(0).getSource().getParameter()),
                () -> assertEquals(SEQUENTIAL_POINTER_RESPONSE, resultResponseErrors.get(0).getSource().getPointer())
        );
    }

    @Test
    public void shouldHandleErrorWithMultipleConstants() {
        Set<DetailedErrorData> detailedErrorDataSet = Sets
                .newSet(new DetailedErrorData(TEST_CONSTANT, ERROR_DETAIL_DATA),
                        new DetailedErrorData(TOURNAMENT_NAME_EMPTY, ERROR_DETAIL_DATA_2),
                        new DetailedErrorData(PLAYER_FIRST_NAME_EMPTY));
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add(ERROR_DETAIL_DATA);
        parameters.add(ERROR_DETAIL_DATA_2);

        ResponseEntity<ErrorResponse> result =
                testInstance.handleDetailedException(new DetailedException(detailedErrorDataSet));
        List<ErrorData> resultResponseErrors = result.getBody().getErrors();

        verify(environmentMock, times(15)).getProperty(any(String.class));
        Assertions.assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> assertEquals(3, resultResponseErrors.size()),
                () -> assertTrue(resultResponseErrors.stream().map(ErrorData::getSource)
                        .map(ErrorDataSource::getParameter).collect(Collectors.toList()).containsAll(parameters)));
    }
}
