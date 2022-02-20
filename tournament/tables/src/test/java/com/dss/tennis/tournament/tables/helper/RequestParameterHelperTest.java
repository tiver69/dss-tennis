package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData.ErrorDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.WarningConstant.REQUEST_PARAMETER_NOT_ALLOWED;
import static com.dss.tennis.tournament.tables.helper.RequestParameterHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestParameterHelperTest {

    private static final String NOT_ALLOWED_VALUE = "NOT_ALLOWED";
    private static final String REQUEST_VALUE_ALL_ALLOWED = CONTEST_VALUE;
    private static final String REQUEST_VALUE_MIX_ALLOWED = CONTEST_VALUE + PARAMETER_SEPARATOR + NOT_ALLOWED_VALUE;
    private static final String REQUEST_VALUE_NOT_ALLOWED = NOT_ALLOWED_VALUE + PARAMETER_SEPARATOR + NOT_ALLOWED_VALUE;

    @Mock
    private WarningHandler warningHandlerMock;
    @Spy
    private ErrorData errorDataSpy;
    @Spy
    private ErrorDataSource errorDataSourceSpy;

    @InjectMocks
    private RequestParameterHelper testInstance;

    @BeforeEach
    void setUp() {
        testInstance.initialize();
    }

    @Test
    public void shouldPopulateRequestParameterWithAllAllowedValues() {
        RequestParameter result = new RequestParameter();

        Set<ErrorData> warnings = testInstance
                .populateRequestParameter(INCLUDE_KEY, REQUEST_VALUE_ALL_ALLOWED, result);

        verify(warningHandlerMock, never()).createWarning(eq(REQUEST_PARAMETER_NOT_ALLOWED), any(String.class));
        Assertions.assertAll(
                () -> Assertions.assertTrue(warnings.isEmpty()),
                () -> Assertions.assertTrue(result.isIncludeContests())
        );
    }

    @Test
    public void shouldPopulateRequestParameterWithMixAllowedValues() {
        when(warningHandlerMock.createWarning(eq(REQUEST_PARAMETER_NOT_ALLOWED), any(String.class)))
                .thenReturn(errorDataSpy);
        when(errorDataSpy.getSource()).thenReturn(errorDataSourceSpy);
        RequestParameter result = new RequestParameter();

        Set<ErrorData> warnings = testInstance
                .populateRequestParameter(INCLUDE_KEY, REQUEST_VALUE_MIX_ALLOWED, result);

        verify(warningHandlerMock).createWarning(REQUEST_PARAMETER_NOT_ALLOWED, NOT_ALLOWED_VALUE);
        verify(errorDataSourceSpy).setPointer(PARAMETER_URL_SEPARATOR + INCLUDE_KEY);
        Assertions.assertAll(
                () -> Assertions.assertFalse(warnings.isEmpty()),
                () -> Assertions.assertEquals(1, warnings.size()),
                () -> Assertions.assertTrue(result.isIncludeContests())
        );
    }

    @Test
    public void shouldPopulateDefaultRequestParameterForNotAllowedValues() {
        when(warningHandlerMock.createWarning(eq(REQUEST_PARAMETER_NOT_ALLOWED), any(String.class)))
                .thenReturn(errorDataSpy);
        when(errorDataSpy.getSource()).thenReturn(errorDataSourceSpy);
        RequestParameter result = new RequestParameter();

        Set<ErrorData> warnings = testInstance
                .populateRequestParameter(INCLUDE_KEY, REQUEST_VALUE_NOT_ALLOWED, result);

        verify(warningHandlerMock).createWarning(REQUEST_PARAMETER_NOT_ALLOWED, NOT_ALLOWED_VALUE);
        verify(errorDataSourceSpy).setPointer(PARAMETER_URL_SEPARATOR + INCLUDE_KEY);
        Assertions.assertAll(
                () -> Assertions.assertFalse(warnings.isEmpty()),
                () -> Assertions.assertEquals(1, warnings.size()),
                () -> Assertions.assertFalse(result.isIncludeContests())
        );
    }
}
