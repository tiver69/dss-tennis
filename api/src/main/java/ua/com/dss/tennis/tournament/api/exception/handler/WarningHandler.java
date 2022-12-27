package ua.com.dss.tennis.tournament.api.exception.handler;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;

@Component
@PropertySource("classpath:warning.properties")
public class WarningHandler extends SourceAwareExceptionHandler {

    protected final String CODE_SUFFIX = ".warning.code";
    protected final String DETAIL_SUFFIX = ".warning.detail";
    protected final String WARNING_SUFFIX = ".warning";

    @Override
    protected ErrorData createErrorDataBase(String errorConstant) {
        return ErrorData.builder()
                .code(environment.getProperty(errorConstant + CODE_SUFFIX))
                .detail(environment.getProperty(errorConstant + DETAIL_SUFFIX))
                .build();
    }

    @Override
    protected String getPointerSuffix() {
        return WARNING_SUFFIX + super.getPointerSuffix();
    }
}
