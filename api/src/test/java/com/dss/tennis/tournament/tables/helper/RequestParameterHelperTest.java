package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.definitions.ErrorResponse.ErrorData;
import com.dss.tennis.tournament.tables.model.definitions.ErrorResponse.ErrorData.ErrorDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestParameterHelperTest {

    private static final String NOT_ALLOWED_VALUE = "NOT_ALLOWED";
//    private static final String REQUEST_VALUE_ALL_ALLOWED = CONTEST_VALUE;
//    private static final String REQUEST_VALUE_MIX_ALLOWED = CONTEST_VALUE + PARAMETER_SEPARATOR + NOT_ALLOWED_VALUE;
//    private static final String REQUEST_VALUE_NOT_ALLOWED = NOT_ALLOWED_VALUE + PARAMETER_SEPARATOR + NOT_ALLOWED_VALUE;

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

//    @Test
//    public void shouldPopulateRequestParameterWithAllAllowedValues() {
//        RequestParameter result = new RequestParameter();
//
//        Set<ErrorDataDTO> warnings = testInstance
//                .populateRequestParameter(INCLUDE_KEY, REQUEST_VALUE_ALL_ALLOWED, result);
//
//        Assertions.assertAll(
//                () -> Assertions.assertTrue(warnings.isEmpty()),
//                () -> Assertions.assertTrue(result.isIncludeContests())
//        );
//    }
//
//    @Test
//    public void shouldPopulateRequestParameterWithMixAllowedValues() {
//        RequestParameter result = new RequestParameter();
//
//        Set<ErrorDataDTO> warnings = testInstance
//                .populateRequestParameter(INCLUDE_KEY, REQUEST_VALUE_MIX_ALLOWED, result);
//
//        Assertions.assertAll(
//                () -> Assertions.assertFalse(warnings.isEmpty()),
//                () -> Assertions.assertEquals(1, warnings.size()),
//                () -> Assertions.assertTrue(result.isIncludeContests())
//        );
//    }
//
//    @Test
//    public void shouldPopulateDefaultRequestParameterForNotAllowedValues() {
//        RequestParameter result = new RequestParameter();
//
//        Set<ErrorDataDTO> warnings = testInstance
//                .populateRequestParameter(INCLUDE_KEY, REQUEST_VALUE_NOT_ALLOWED, result);
//
//        Assertions.assertAll(
//                () -> Assertions.assertFalse(warnings.isEmpty()),
//                () -> Assertions.assertEquals(1, warnings.size()),
//                () -> Assertions.assertFalse(result.isIncludeContests())
//        );
//    }
}
