package ua.com.dss.tennis.tournament.api.exception.handler;

import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;

public interface JacksonExceptionHandler {
    ErrorData createErrorData(Throwable exception);
}
