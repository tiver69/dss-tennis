package ua.com.dss.tennis.tournament.api.exception.handler;

import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.GENERAL_BAD_REQUEST;

@Service
public class DefaultExceptionHandler extends SourceAwareExceptionHandler implements JacksonExceptionHandler {

    @Override
    public ErrorData createErrorData(Throwable exception) {
        return createErrorData(new ErrorDataDTO(GENERAL_BAD_REQUEST, exception.getMessage()));
    }
}
