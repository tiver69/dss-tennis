package ua.com.dss.tennis.tournament.api.exception.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.GENERAL_BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class DefaultExceptionHandlerTest {

    private static final String CODE_DETAIL = "Code_Detail";
    private static final String ANY_DETAIL = "Detail";
    private static final String RUNTIME_MESSAGE = "Runtime message";
    private final String CODE_SUFFIX = ".code";


    @Mock
    private Environment environmentMock;

    @InjectMocks
    private DefaultExceptionHandler testInstance;


    @Test
    public void shouldCreateErrorData() {
        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);
        when(environmentMock.getProperty(GENERAL_BAD_REQUEST + CODE_SUFFIX)).thenReturn(CODE_DETAIL);

        ErrorData result = testInstance.createErrorData(new Throwable(RUNTIME_MESSAGE));

        verify(environmentMock, times(5)).getProperty(any(String.class));
        Assertions.assertAll(
                () -> assertEquals(CODE_DETAIL, result.getCode()),
                () -> assertEquals(RUNTIME_MESSAGE, result.getSource().getParameter())
        );
    }
}
