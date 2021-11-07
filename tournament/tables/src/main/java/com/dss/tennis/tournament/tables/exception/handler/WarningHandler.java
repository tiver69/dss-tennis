package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.exception.error.WarningConstant;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData.ErrorDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:warning.properties")
public class WarningHandler {

    protected final String CODE_SUFFIX = ".code";
    protected final String DETAIL_SUFFIX = ".detail";
    protected final String POINTER_SUFFIX = ".pointer";

    @Autowired
    private Environment environment;

    public ErrorData createWarning(WarningConstant warningConstant, String detailedParameter) {
        return createWarning(warningConstant, detailedParameter, null);
    }

    public ErrorData createWarning(WarningConstant warningConstant, Byte sequentNumber) {
        return createWarning(warningConstant, null, sequentNumber);
    }

    public ErrorData createWarning(WarningConstant warningConstant, String detailedParameter,
                                   Byte sequentNumber) {
        String errorConstant = warningConstant.toString();
        ErrorDataSource errorDataSource = constructErrorDataSource(errorConstant, detailedParameter, sequentNumber);

        return ErrorData.builder()
                .code(environment.getProperty(errorConstant + CODE_SUFFIX))
                .detail(environment.getProperty(errorConstant + DETAIL_SUFFIX))
                .source(errorDataSource)
                .build();
    }

    private ErrorDataSource constructErrorDataSource(String errorConstant, String detailParameter, Byte sequentNumber) {
        String sequentialPointerString = constructSequentialPointer(errorConstant, sequentNumber);
        if (StringUtils.isBlank(detailParameter) && StringUtils.isBlank(sequentialPointerString)) {
            return null;
        }

        return ErrorDataSource.builder()
                .parameter(detailParameter)
                .pointer(sequentialPointerString)
                .build();
    }

    private String constructSequentialPointer(String errorConstant, Byte pointer) {
        String sequentialPointerFormat = environment.getProperty(errorConstant + POINTER_SUFFIX);
        return pointer == null ? sequentialPointerFormat :
                String.format(sequentialPointerFormat, pointer);
    }
}
