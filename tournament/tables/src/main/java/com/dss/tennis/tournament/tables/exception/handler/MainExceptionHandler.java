package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.error.ErrorConstants.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class MainExceptionHandler extends SourceAwareExceptionHandler {

    private Map<Class<? extends Throwable>, JacksonExceptionHandler> jacksonExceptionHandlerMap;

    @Autowired
    private InvalidFormatExceptionHandler invalidFormatExceptionHandler;
    @Autowired
    private JsonParseExceptionHandler jsonParseExceptionHandler;
    @Autowired
    private DefaultExceptionHandler defaultExceptionHandler;

    @PostConstruct
    protected void initialize() {
        jacksonExceptionHandlerMap = new HashMap<>();
        jacksonExceptionHandlerMap.put(InvalidFormatException.class, invalidFormatExceptionHandler);
        jacksonExceptionHandlerMap.put(JsonParseException.class, jsonParseExceptionHandler);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        ErrorData errorData = createErrorData(new DetailedErrorData(INTERNAL_SERVER_ERROR, exception.getMessage()));
        System.out.println(exception);
        return new ResponseEntity<>(new ErrorResponse(errorData), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DetailedException.class)
    public ResponseEntity<ErrorResponse> handleDetailedException(DetailedException exception) {
        List<ErrorData> list =
                exception.getErrors().stream().map(this::createErrorData)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResponse(list), getHttpStatus(list));
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        JacksonExceptionHandler handler = jacksonExceptionHandlerMap
                .getOrDefault(exception.getCause().getClass(), defaultExceptionHandler);

        ErrorData errorData = handler.createErrorData(exception.getCause());
        return new ResponseEntity<>(new ErrorResponse(errorData), HttpStatus.BAD_REQUEST);
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
