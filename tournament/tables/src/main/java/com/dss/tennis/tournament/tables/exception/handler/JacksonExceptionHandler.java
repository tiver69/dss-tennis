package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;

public interface JacksonExceptionHandler {
    ErrorData createErrorData(Throwable exception);
}
