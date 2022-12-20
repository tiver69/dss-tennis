package com.dss.tennis.tournament.tables.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class InvalidFormatExceptionHandlerTest {

    private static final String CODE_DETAIL = "Code_Detail";
    private static final String ANY_DETAIL = "Detail";
    private static final String EXCEPTION_VALUE = "Runtime message";
    private static final String EXCEPTION_PATH = "line 1, symbol 1";
    private static final String EXCEPTION_PATH_FULL = "Exception path [\"" + EXCEPTION_PATH + "\"]";
    protected final String CODE_SUFFIX = ".code";

    @Mock
    private Environment environmentMock;
    @Mock
    private InvalidFormatException invalidFormatExceptionMock;

    @Spy
    private Reference referenceSpy;

    @InjectMocks
    private InvalidFormatExceptionHandler testInstance;
//
//    @Test
//    public void shouldCreateErrorDataWithParameterAndPointer() {
//        when(invalidFormatExceptionMock.getValue()).thenReturn(EXCEPTION_VALUE);
//        when(invalidFormatExceptionMock.getPath()).thenReturn(List.of(referenceSpy));
//        when(referenceSpy.toString()).thenReturn(EXCEPTION_PATH_FULL);
//
//        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);
//        when(environmentMock.getProperty(GENERAL_BAD_REQUEST + CODE_SUFFIX)).thenReturn(CODE_DETAIL);
//
//        ErrorData result = testInstance.createErrorData(invalidFormatExceptionMock);
//
//        verify(environmentMock, times(4)).getProperty(any(String.class));
//        Assertions.assertAll(
//                () -> assertEquals(CODE_DETAIL, result.getCode()),
//                () -> assertEquals(EXCEPTION_VALUE, result.getSource().getParameter()),
//                () -> assertEquals("/" + EXCEPTION_PATH, result.getSource().getPointer())
//        );
//    }

//    @Test
//    public void shouldCreateErrorDataWithoutParameterAndPointer() {
//        when(invalidFormatExceptionMock.getValue()).thenReturn("");
//        when(invalidFormatExceptionMock.getPath()).thenReturn(List.of(referenceSpy));
//        when(referenceSpy.toString()).thenReturn("");
//
//        when(environmentMock.getProperty(any(String.class))).thenReturn(ANY_DETAIL);
//        when(environmentMock.getProperty(GENERAL_BAD_REQUEST + CODE_SUFFIX)).thenReturn(CODE_DETAIL);
//
//        ErrorData result = testInstance.createErrorData(invalidFormatExceptionMock);
//
//        verify(environmentMock, times(4)).getProperty(any(String.class));
//        Assertions.assertAll(
//                () -> assertEquals(CODE_DETAIL, result.getCode()),
//                () -> assertNull(result.getSource())
//        );
//    }
}