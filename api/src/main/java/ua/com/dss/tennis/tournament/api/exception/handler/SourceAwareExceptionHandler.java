package ua.com.dss.tennis.tournament.api.exception.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData.ErrorDataSource;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;

@PropertySource("classpath:error.properties")
public abstract class SourceAwareExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    protected Environment environment;

    protected final String STATUS_SUFFIX = ".status";
    protected final String CODE_SUFFIX = ".code";
    protected final String TITLE_SUFFIX = ".title";
    protected final String DETAIL_SUFFIX = ".detail";
    protected final String POINTER_SUFFIX = ".pointer";

    protected final String SEQUENTIAL_POINTER_KEY = "%o";
    protected final String STRING_POINTER_KEY = "%s";

    public ErrorData createErrorData(ErrorDataDTO currentError) {
        String errorConstant = currentError.getErrorKey().toString();
        ErrorDataSource errorDataSource = constructErrorDataSource(errorConstant, currentError
                .getDetailParameter(), currentError.getPointer(), currentError.getSequentNumber());

        ErrorData errorData = createErrorDataBase(errorConstant);
        errorData.setSource(errorDataSource);
        return errorData;
    }

    protected ErrorData createErrorData(ErrorKey errorConstantEnum, String detailParameter, String pointer) {
        String errorConstant = errorConstantEnum.toString();
        ErrorData errorData = createErrorDataBase(errorConstant);
        errorData.setSource(constructErrorDataSource(errorConstant, detailParameter, pointer, null));
        return errorData;
    }

    protected ErrorData createErrorDataBase(String errorConstant) {
        return ErrorData.builder()
                .status(environment.getProperty(errorConstant + STATUS_SUFFIX))
                .code(environment.getProperty(errorConstant + CODE_SUFFIX))
                .title(environment.getProperty(errorConstant + TITLE_SUFFIX))
                .detail(environment.getProperty(errorConstant + DETAIL_SUFFIX))
                .build();
    }

    protected ErrorDataSource constructErrorDataSource(String errorConstant, String detailParameter,
                                                       String customPointer, Byte sequentNumber) {
        String pointer = constructPointer(errorConstant, customPointer, sequentNumber);
        return StringUtils.isBlank(detailParameter) && StringUtils.isBlank(pointer) ?
                null :
                ErrorDataSource.builder()
                        .parameter(detailParameter)
                        .pointer(pointer)
                        .build();
    }

    protected String constructPointer(String errorConstant, String pointerString, Byte sequenceNumber) {
        String pointerFormat = environment.getProperty(errorConstant + getPointerSuffix());
        if (pointerFormat == null) return pointerString;
        if (pointerString == null && sequenceNumber == null) return pointerFormat;
        if (pointerFormat.contains(SEQUENTIAL_POINTER_KEY) && sequenceNumber != null)
            return String.format(pointerFormat, sequenceNumber);
        if (pointerFormat.contains(STRING_POINTER_KEY) && pointerString != null && !pointerString.isEmpty())
            return String.format(pointerFormat, pointerString);

        throw new IllegalArgumentException("Error pointer misconfiguration, please check property file and error data");
    }

    protected String getPointerSuffix() {
        return POINTER_SUFFIX;
    }
}
