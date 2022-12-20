package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.model.definitions.ErrorResponse.ErrorData;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import org.springframework.stereotype.Service;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.GENERAL_BAD_REQUEST;

@Service
public class DefaultExceptionHandler extends SourceAwareExceptionHandler implements JacksonExceptionHandler {

    @Override
    public ErrorData createErrorData(Throwable exception) {
        return createErrorData(new ErrorDataDTO(GENERAL_BAD_REQUEST, exception.getMessage()));
    }
}
