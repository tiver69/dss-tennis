package ua.com.dss.tennis.tournament.api.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;

@Service
public class JsonParseExceptionHandler extends SourceAwareExceptionHandler implements JacksonExceptionHandler {

    protected final String FIRST_SEPARATOR = ": ";
    protected final String SECOND_SEPARATOR = "\n";
    protected final String LOCATION_OPEN = "; ";
    protected final String LOCATION_CLOSE = "]";

    /**
     * @param exception contains message of format:
     *                  JSON parse error: Unexpected character ('"' (code 34)): was expecting comma to separate
     *                  Object entries\n
     */
    @Override
    public ErrorData createErrorData(Throwable exception) {
        JsonParseException castedException = (JsonParseException) exception;
        String detail = StringUtils.substringBetween(castedException.getMessage(), FIRST_SEPARATOR, SECOND_SEPARATOR);
        String parameter = StringUtils.substringBefore(castedException.getMessage(), FIRST_SEPARATOR);
        String pointer = StringUtils
                .substringBetween(castedException.getLocation().toString(), LOCATION_OPEN, LOCATION_CLOSE);

        ErrorData errorData = super.createErrorData(ErrorConstants.ErrorKey.GENERAL_BAD_REQUEST, parameter, pointer);
        errorData.setDetail(StringUtils.capitalize(detail));
        return errorData;
    }
}
