package ua.com.dss.tennis.tournament.api.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.modelmapper.MappingException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.AUTHENTICATION_FAILED;
import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class MainExceptionHandler extends SourceAwareExceptionHandler {

    private Map<Class<? extends Throwable>, JacksonExceptionHandler> jacksonExceptionHandlerMap;

    @Autowired
    private InvalidFormatExceptionHandler invalidFormatExceptionHandler;
    @Autowired
    private JsonParseExceptionHandler jsonParseExceptionHandler;
    @Autowired
    private TypeMismatchExceptionHandler typeMismatchExceptionHandler;
    @Autowired
    private DefaultExceptionHandler defaultExceptionHandler;

    @PostConstruct
    protected void initialize() {
        jacksonExceptionHandlerMap = new HashMap<>();
        jacksonExceptionHandlerMap.put(InvalidFormatException.class, invalidFormatExceptionHandler);
        jacksonExceptionHandlerMap.put(MethodArgumentTypeMismatchException.class, typeMismatchExceptionHandler);
        jacksonExceptionHandlerMap.put(JsonParseException.class, jsonParseExceptionHandler);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        ErrorData errorData = createErrorData(new ErrorDataDTO(INTERNAL_SERVER_ERROR, exception.getMessage()));
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse(errorData), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exception) {
        return new ResponseEntity<>(new ErrorResponse(createErrorData(AUTHENTICATION_FAILED, null, null)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MappingException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorResponse> handleRuntimeExceptionWithDetailedCause(MappingException exception) {
        return exception.getCause() instanceof DetailedException ?
                handleDetailedException((DetailedException) exception.getCause()) : handleRuntimeException(exception);
    }

    @ExceptionHandler(DetailedException.class)
    public ResponseEntity<ErrorResponse> handleDetailedException(DetailedException exception) {
        List<ErrorData> list =
                exception.getErrors().stream().map(this::createErrorData)
                        .collect(Collectors.toList());
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse(list), getHttpStatus(list));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {
        JacksonExceptionHandler handler = jacksonExceptionHandlerMap
                .getOrDefault(exception.getClass(), defaultExceptionHandler);

        ErrorData errorData = handler.createErrorData(exception);
        return new ResponseEntity<>(new ErrorResponse(errorData), HttpStatus.BAD_REQUEST);
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

    public HttpStatus getHttpStatus(List<ErrorData> list) {
        return list.stream().findFirst().map(ErrorData::getCode).map(Integer::parseInt).map(HttpStatus::resolve)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
