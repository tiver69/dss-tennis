package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.exception.error.ErrorConstants;
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
    private Environment environment;

    protected final String STATUS_SUFFIX = ".status";
    protected final String CODE_SUFFIX = ".code";
    protected final String TITLE_SUFFIX = ".title";
    protected final String DETAIL_SUFFIX = ".detail";
    protected final String POINTER_SUFFIX = ".pointer";

    protected ErrorData createErrorData(DetailedErrorData currentError) {
        String errorConstant = currentError.getErrorConstant().toString();
        ErrorDataSource errorDataSource = constructErrorDataSource(errorConstant, currentError
                .getDetailParameter(), currentError.getSequentNumber());

        ErrorData errorData = createErrorDataBase(errorConstant);
        errorData.setSource(errorDataSource);
        return errorData;
    }

    protected ErrorData createErrorData(ErrorConstants errorConstantEnum, String detailParameter, String pointer) {
        String errorConstant = errorConstantEnum.toString();

        ErrorData errorData = createErrorDataBase(errorConstant);
        errorData.setSource(constructErrorDataSource(detailParameter, pointer));
        return errorData;
    }

    private ErrorData createErrorDataBase(String errorConstant) {
        return ErrorData.builder()
                .status(environment.getProperty(errorConstant + STATUS_SUFFIX))
                .code(environment.getProperty(errorConstant + CODE_SUFFIX))
                .title(environment.getProperty(errorConstant + TITLE_SUFFIX))
                .detail(environment.getProperty(errorConstant + DETAIL_SUFFIX))
                .build();
    }

    private ErrorDataSource constructErrorDataSource(String errorConstant, String detailParameter, Byte sequentNumber) {
        String sequentialPointerString = constructSequentialPointer(errorConstant, sequentNumber);
        return constructErrorDataSource(detailParameter, sequentialPointerString);
    }

    private ErrorDataSource constructErrorDataSource(String detailParameter, String pointer) {
        if (StringUtils.isBlank(detailParameter) && StringUtils.isBlank(pointer))
            return null;

        return ErrorDataSource.builder()
                .parameter(detailParameter)
                .pointer(pointer)
                .build();
    }

    private String constructSequentialPointer(String errorConstant, Byte pointer) {
        String sequentialPointerFormat = environment.getProperty(errorConstant + POINTER_SUFFIX);
        return pointer == null ? sequentialPointerFormat :
                String.format(sequentialPointerFormat, pointer);
    }
}
