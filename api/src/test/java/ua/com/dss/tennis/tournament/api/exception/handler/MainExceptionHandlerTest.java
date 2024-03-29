package ua.com.dss.tennis.tournament.api.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData.ErrorDataSource;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.PLAYER_FIRST_NAME_EMPTY;
import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.TOURNAMENT_NAME_EMPTY;

@ExtendWith(MockitoExtension.class)
class MainExceptionHandlerTest {

    private static final ErrorKey TEST_CONSTANT = ErrorConstants.ErrorKey.TOURNAMENT_NAME_DUPLICATE;
    private static final String RUNTIME_MESSAGE = "Runtime message";
    private static final String ANY_DETAIL = "Detail";
    private static final String ERROR_DETAIL_DATA = "DetailData";
    private static final String ERROR_DETAIL_DATA_2 = "DetailData2";
    private static final String CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private static final String POINTER_SUFFIX = ".pointer";
    private static final String SEQUENTIAL_POINTER_FORMAT = "[%o]";
    private static final String SEQUENTIAL_POINTER_RESPONSE = "[1]";
    private static final Byte SEQUENCE_NUMBER = 1;

    @Mock
    private InvalidFormatExceptionHandler invalidFormatExceptionHandlerMock;
    @Mock
    private JsonParseExceptionHandler jsonParseExceptionHandlerMock;
    @Mock
    private DefaultExceptionHandler defaultExceptionHandlerMock;
    @Mock
    private Environment environmentMock;

    @Spy
    private ErrorData errorDataSpy;
    @Mock
    private HttpMessageNotReadableException exceptionSpy;

    @InjectMocks
    private MainExceptionHandler testInstance;

    @Test
    public void shouldHandleRuntimeException() {
        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);

        ResponseEntity<ErrorResponse> result =
                testInstance.handleRuntimeException(new RuntimeException(RUNTIME_MESSAGE));
        List<ErrorData> resultResponseErrors = result.getBody().getErrors();

        verify(environmentMock, times(5)).getProperty(any(String.class));
        Assertions.assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode()),
                () -> assertEquals(1, resultResponseErrors.size()),
                () -> assertEquals(RUNTIME_MESSAGE, resultResponseErrors.get(0).getSource().getParameter())
        );
    }

    @Test
    public void shouldHandleErrorWithSingleConstant() {
        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);
        when(environmentMock.getProperty(TEST_CONSTANT + testInstance.CODE_SUFFIX)).thenReturn(CODE);

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
        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);
        when(environmentMock.getProperty(TEST_CONSTANT + testInstance.CODE_SUFFIX)).thenReturn(CODE);
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
        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);

        when(environmentMock.getProperty(TEST_CONSTANT + testInstance.CODE_SUFFIX)).thenReturn(CODE);
        Set<ErrorDataDTO> detailedErrorDataSet = Sets
                .newSet(new ErrorDataDTO(TEST_CONSTANT, ERROR_DETAIL_DATA),
                        new ErrorDataDTO(TOURNAMENT_NAME_EMPTY, ERROR_DETAIL_DATA_2),
                        new ErrorDataDTO(PLAYER_FIRST_NAME_EMPTY));
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


    @Test
    public void shouldHandleHttpMessageNotReadableWithCause() {
        testInstance.initialize();
        InvalidFormatException invalidFormatException = new InvalidFormatException(null, RUNTIME_MESSAGE, null,
                null);
        when(exceptionSpy.getCause()).thenReturn(invalidFormatException);
        when(invalidFormatExceptionHandlerMock.createErrorData(invalidFormatException)).thenReturn(errorDataSpy);

        ResponseEntity<Object> result = testInstance
                .handleHttpMessageNotReadable(exceptionSpy, null, null, null);
        List<ErrorData> resultResponseErrors = ((ErrorResponse) result.getBody()).getErrors();

        verify(invalidFormatExceptionHandlerMock).createErrorData(invalidFormatException);
        Assertions.assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> assertEquals(1, resultResponseErrors.size()),
                () -> assertEquals(errorDataSpy, resultResponseErrors.get(0))
        );
    }
}
