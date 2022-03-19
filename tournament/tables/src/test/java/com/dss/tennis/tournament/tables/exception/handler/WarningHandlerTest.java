package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarningHandlerTest {

    private static final ErrorConstants TEST_CONSTANT = ErrorConstants.PARTICIPANT_DUPLICATION;
    private static final String ANY_DETAIL = "Detail";
    private static final String POINTER_SUFFIX = ".warning.pointer";
    private static final String WARNING_PARAMETER = "parameter";
    private static final Byte SEQUENCE_NUMBER = 1;
    private static final String SEQUENTIAL_POINTER_FORMAT = "[%o]";
    private static final String SEQUENTIAL_POINTER_RESPONSE = "[1]";

    @Mock
    private Environment environmentMock;

    @InjectMocks
    private WarningHandler testInstance;

    @BeforeEach
    void setUp() {
        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);
        when(environmentMock.getProperty(TEST_CONSTANT + POINTER_SUFFIX)).thenReturn(SEQUENTIAL_POINTER_FORMAT);
    }

//    @Test
//    public void shouldCreateWarningWithoutParameter() {
//        ErrorData result = testInstance.createErrorData(TEST_CONSTANT, SEQUENCE_NUMBER);
//
//        verify(environmentMock, times(3)).getProperty(any(String.class));
//        Assertions.assertAll(
//                () -> assertEquals(ANY_DETAIL, result.getCode()),
//                () -> assertEquals(ANY_DETAIL, result.getDetail()),
//                () -> assertNull(result.getSource().getParameter()),
//                () -> assertEquals(SEQUENTIAL_POINTER_RESPONSE, result.getSource().getPointer())
//        );
//    }
//
//    @Test
//    public void shouldCreateWarningWithoutErrorDataSource() {
//        when(environmentMock.getProperty(TEST_CONSTANT + POINTER_SUFFIX)).thenReturn("");
//
//        ErrorData result = testInstance.createErrorData(TEST_CONSTANT, SEQUENCE_NUMBER);
//
//        verify(environmentMock, times(3)).getProperty(any(String.class));
//        Assertions.assertAll(
//                () -> assertEquals(ANY_DETAIL, result.getCode()),
//                () -> assertEquals(ANY_DETAIL, result.getDetail()),
//                () -> assertNull(result.getSource())
//        );
//    }
//
//    @Test
//    public void shouldCreateWarningWithParameterWithoutSequenceNumber() {
//        ErrorData result = testInstance.createErrorData(TEST_CONSTANT, WARNING_PARAMETER);
//
//        verify(environmentMock, times(3)).getProperty(any(String.class));
//        Assertions.assertAll(
//                () -> assertEquals(ANY_DETAIL, result.getCode()),
//                () -> assertEquals(ANY_DETAIL, result.getDetail()),
//                () -> assertEquals(WARNING_PARAMETER, result.getSource().getParameter()),
//                () -> assertEquals(SEQUENTIAL_POINTER_FORMAT, result.getSource().getPointer())
//        );
//    }
}
