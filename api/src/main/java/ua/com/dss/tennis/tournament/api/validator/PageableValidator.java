package ua.com.dss.tennis.tournament.api.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey;
import ua.com.dss.tennis.tournament.api.helper.RequestParameterHelper;
import ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;

import java.util.Set;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.*;
import static ua.com.dss.tennis.tournament.api.helper.RequestParameterHelper.PARAMETER_URL_SEPARATOR;

@Component
public class PageableValidator {

    public static final String PAGE_DEFAULT_STRING = "0";  //Spring numerates pages from 0
    public static final String PAGE_SIZE_DEFAULT_STRING = "5";

    public static String PAGE_KEY = "page";
    public static String PAGE_SIZE_KEY = "pageSize";

    @Autowired
    private RequestParameterHelper requestParameterHelper;

    public Pageable validatePageableRequest(int page, byte pageSize, Set<ErrorDataDTO> warnings,
                                            ResourceObjectType resourceType) {
        if (page < 0) {
            ErrorDataDTO loverLimitWarning = createWarningsForNotAllowedParameter(PAGE_OUT_OF_LOWER_RANGE, String
                    .valueOf(page + 1), PAGE_KEY);
            warnings.add(loverLimitWarning);
            page = Integer.parseInt(PAGE_DEFAULT_STRING);
        }

        if (pageSize > 10 || pageSize < 0) {
            ErrorDataDTO pageSizeWarning = createWarningsForNotAllowedParameter(PAGE_SIZE_OUT_OF_RANGE, String
                    .valueOf(pageSize), PAGE_SIZE_KEY);
            warnings.add(pageSizeWarning);
            pageSize = Byte.parseByte(PAGE_SIZE_DEFAULT_STRING);
        }

        return requestParameterHelper.populatePageableRequestParameter(page, pageSize, resourceType);
    }

    public Pageable validateUpperPage(Pageable pageableRequestParameter, int maxPage, Set<ErrorDataDTO> warnings,
                                      ResourceObjectType resourceType) {
        int currentPageRequest = pageableRequestParameter.getPageNumber();
        if (currentPageRequest >= maxPage) {
            ErrorDataDTO pageSizeWarning = createWarningsForNotAllowedParameter(PAGE_OUT_OF_UPPER_RANGE, String
                    .valueOf(currentPageRequest + 1), PAGE_KEY);
            warnings.add(pageSizeWarning);
            return requestParameterHelper
                    .populatePageableRequestParameter(maxPage - 1, pageableRequestParameter
                            .getPageSize(), resourceType);
        }
        return null;
    }

    private ErrorDataDTO createWarningsForNotAllowedParameter(ErrorKey key, String notAllowedValue,
                                                              String notAllowedKey) {
        return ErrorDataDTO.builder().errorKey(key).detailParameter(notAllowedValue)
                .pointer(PARAMETER_URL_SEPARATOR + notAllowedKey).build();
    }
}