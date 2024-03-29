package ua.com.dss.tennis.tournament.api.exception.handler;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class JsonParseExceptionHandlerTest {

    private static final String CODE_DETAIL = "Code_Detail";
    private static final String ANY_DETAIL = "Detail";
    private static final String EXCEPTION_DETAIL = "exception message";
    private static final String EXCEPTION_PARAMETER = "Full part of";
    private static final String EXCEPTION_MESSAGE_FULL = EXCEPTION_PARAMETER + ": " + EXCEPTION_DETAIL + "\n";
    private static final String POINTER = "line 1, symbol 1";
    private static final String LOCATION_FULL = "Location path ; " + POINTER + "]";
    protected final String CODE_SUFFIX = ".code";

    @Mock
    private Environment environmentMock;
    @Mock
    private JsonParseException jsonParseExceptionMock;
    @Mock
    private JsonLocation jsonLocationMock;

    @InjectMocks
    private JsonParseExceptionHandler testInstance;

//    @Test
//    public void shouldCreateErrorDataWithParameterAndPointer() {
//        when(jsonParseExceptionMock.getMessage()).thenReturn(EXCEPTION_MESSAGE_FULL);
//        when(jsonParseExceptionMock.getLocation()).thenReturn(jsonLocationMock);
//        when(jsonLocationMock.toString()).thenReturn(LOCATION_FULL);
//
//        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);
//        when(environmentMock.getProperty(GENERAL_BAD_REQUEST + CODE_SUFFIX)).thenReturn(CODE_DETAIL);
//
//        ErrorData result = testInstance.createErrorData(jsonParseExceptionMock);
//
//        verify(environmentMock, times(4)).getProperty(any(String.class));
//        Assertions.assertAll(
//                () -> assertEquals(CODE_DETAIL, result.getCode()),
//                () -> assertEquals(StringUtils.capitalize(EXCEPTION_DETAIL), result.getDetail()),
//                () -> assertEquals(EXCEPTION_PARAMETER, result.getSource().getParameter()),
//                () -> assertEquals(POINTER, result.getSource().getPointer())
//        );
//    }
}