package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.error.WarningConstant;
import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.helper.RequestParameterHelper;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.dss.tennis.tournament.tables.exception.error.WarningConstant.*;
import static com.dss.tennis.tournament.tables.helper.RequestParameterHelper.PARAMETER_URL_SEPARATOR;

@Component
public class PageableValidator {

    public static final String PAGE_DEFAULT_STRING = "1";
    public static final String PAGE_SIZE_DEFAULT_STRING = "5";

    public static String PAGE_KEY = "page";
    public static String PAGE_SIZE_KEY = "pageSize";

    @Autowired
    private WarningHandler warningHandler;
    @Autowired
    private RequestParameterHelper requestParameterHelper;

    public Pageable validatePageableRequest(int page, byte pageSize, Set<ErrorData> warnings) {
        if (page < 0) {
            ErrorData loverLimitWarning = createWarningsForNotAllowedParameter(PAGE_OUT_OF_LOWER_RANGE, String
                    .valueOf(page), PAGE_KEY);
            warnings.add(loverLimitWarning);
            page = Integer.parseInt(PAGE_DEFAULT_STRING);
        }

        if (pageSize > 10 || pageSize < 0) {
            ErrorData pageSizeWarning = createWarningsForNotAllowedParameter(PAGE_SIZE_OUT_OF_RANGE, String
                    .valueOf(pageSize), PAGE_SIZE_KEY);
            warnings.add(pageSizeWarning);
            pageSize = Byte.parseByte(PAGE_SIZE_DEFAULT_STRING);
        }

        return requestParameterHelper.populatePageableRequestParameter(page, pageSize);
    }

    public Pageable validateUpperPage(Pageable pageableRequestParameter, int maxPage, Set<ErrorData> warnings) {
        int currentPageRequest = pageableRequestParameter.getPageNumber();
        if (currentPageRequest >= maxPage) {
            ErrorData pageSizeWarning = createWarningsForNotAllowedParameter(PAGE_OUT_OF_UPPER_RANGE, String
                    .valueOf(currentPageRequest), PAGE_KEY);
            warnings.add(pageSizeWarning);
            return requestParameterHelper
                    .populatePageableRequestParameter(maxPage - 1, pageableRequestParameter.getPageSize());
        }
        return null;
    }

    private ErrorData createWarningsForNotAllowedParameter(WarningConstant key, String notAllowedValue,
                                                           String notAllowedKey) {
        ErrorData warning = warningHandler.createWarning(key, notAllowedValue);
        warning.getSource().setPointer(PARAMETER_URL_SEPARATOR + notAllowedKey);
        return warning;
    }
}