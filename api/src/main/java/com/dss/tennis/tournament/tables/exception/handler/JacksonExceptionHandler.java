package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.model.definitions.ErrorResponse.ErrorData;

public interface JacksonExceptionHandler {
    ErrorData createErrorData(Throwable exception);
}
