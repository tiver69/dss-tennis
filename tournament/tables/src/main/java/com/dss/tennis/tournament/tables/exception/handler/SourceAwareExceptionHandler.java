package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.exception.ErrorConstants;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData.ErrorDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@PropertySource("classpath:error.properties")
public abstract class SourceAwareExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    protected Environment environment;

    protected final String STATUS_SUFFIX = ".status";
    protected final String CODE_SUFFIX = ".code";
    protected final String TITLE_SUFFIX = ".title";
    protected final String DETAIL_SUFFIX = ".detail";
    protected final String POINTER_SUFFIX = ".pointer";

    public ErrorData createErrorData(ErrorDataDTO currentError) {
        String errorConstant = currentError.getErrorConstant().toString();
        ErrorDataSource errorDataSource = constructErrorDataSource(errorConstant, currentError
                .getDetailParameter(), currentError.getPointer(), currentError.getSequentNumber());

        ErrorData errorData = createErrorDataBase(errorConstant);
        errorData.setSource(errorDataSource);
        return errorData;
    }

    protected ErrorData createErrorData(ErrorConstants errorConstantEnum, String detailParameter, String pointer) {
        ErrorData errorData = createErrorDataBase(errorConstantEnum.toString());
        errorData.setSource(constructErrorDataSource(detailParameter, pointer));
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
        String pointer = customPointer == null ? constructPointer(errorConstant, sequentNumber) :
                customPointer;

        return constructErrorDataSource(detailParameter, pointer);
    }

    private ErrorDataSource constructErrorDataSource(String detailParameter, String pointer) {
        return StringUtils.isBlank(detailParameter) && StringUtils.isBlank(pointer) ?
                null :
                ErrorDataSource.builder()
                        .parameter(detailParameter)
                        .pointer(pointer)
                        .build();
    }

    protected String constructPointer(String errorConstant, Byte pointer) {
        String sequentialPointerFormat = environment.getProperty(errorConstant + getPointerSuffix());
        return pointer == null ? sequentialPointerFormat :
                String.format(sequentialPointerFormat, pointer);
    }

    protected String getPointerSuffix() {
        return POINTER_SUFFIX;
    }
}
