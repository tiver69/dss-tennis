package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData.ErrorDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@PropertySource("classpath:error.properties")
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    protected final String STATUS_SUFFIX = ".status";
    protected final String CODE_SUFFIX = ".code";
    protected final String TITLE_SUFFIX = ".title";
    protected final String DETAIL_SUFFIX = ".detail";
    protected final String POINTER_SUFFIX = ".pointer";

    @Autowired
    private Environment environment;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        ErrorData errorData = createErrorData(new DetailedErrorData(INTERNAL_SERVER_ERROR, exception.getMessage()));

        return new ResponseEntity<>(new ErrorResponse(errorData), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DetailedException.class)
    public ResponseEntity<ErrorResponse> handleDetailedException(DetailedException exception) {
        List<ErrorData> list =
                exception.getErrors().stream().map(this::createErrorData)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResponse(list), getHttpStatus(list));
    }

    private ErrorData createErrorData(DetailedErrorData currentError) {
        String errorConstant = currentError.getErrorConstant().toString();
        ErrorDataSource errorDataSource = ErrorDataSource.builder()
                .parameter(currentError.getDetailParameter())
                .pointer(constructSequentialPointer(errorConstant, currentError.getSequentNumber()))
                .build();

        return ErrorData.builder()
                .status(environment.getProperty(errorConstant + STATUS_SUFFIX))
                .code(environment.getProperty(errorConstant + CODE_SUFFIX))
                .title(environment.getProperty(errorConstant + TITLE_SUFFIX))
                .detail(environment.getProperty(errorConstant + DETAIL_SUFFIX))
                .source(errorDataSource)
                .build();
    }

    private String constructSequentialPointer(String errorConstant, Byte pointer) {
        String sequentialPointerFormat = environment.getProperty(errorConstant + POINTER_SUFFIX);
        return pointer == null ? sequentialPointerFormat :
                String.format(sequentialPointerFormat, pointer);
    }

    private HttpStatus getHttpStatus(List<ErrorData> list) {
        return list.stream().findFirst().map(ErrorData::getCode).map(Integer::parseInt).map(HttpStatus::resolve)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    protected static class ErrorResponse {

        public ErrorResponse(ErrorData errorData) {
            this.errors = Collections.singletonList(errorData);
        }

        private final List<ErrorData> errors;
    }
}
