package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.REQUEST_PARAMETER_NOT_ALLOWED;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
public class RequestParameterHelper {

    public static String PARAMETER_URL_SEPARATOR = "?";
    public static String PARAMETER_SEPARATOR = ",";
    public static String INCLUDE_KEY = "include";
    public static String CONTEST_VALUE = "contests";
    public static String PLAYERS_VALUE = "players";

    private final Map<String, List<String>> allowedParameters = new HashMap<>();
    private final Map<ResourceObjectType, List<Order>> pageableSortParameters = new HashMap<>();
    private final Map<String, Consumer<Boolean>> requestParameterSetter = new HashMap<>();
    private RequestParameter tmpRequestParameter;

    @Autowired
    private WarningHandler warningHandler;

    @PostConstruct
    protected void initialize() {
        allowedParameters.put(INCLUDE_KEY, List.of(CONTEST_VALUE, PLAYERS_VALUE));

        pageableSortParameters.put(ResourceObjectType.TOURNAMENT, List
                .of(new Order(DESC, "status"), new Order(DESC, "beginningDate")));
        pageableSortParameters.put(ResourceObjectType.PLAYER, List
                .of(new Order(ASC, "lastName"), new Order(ASC, "firstName")));

        tmpRequestParameter = new RequestParameter();
        requestParameterSetter.put(CONTEST_VALUE, tmpRequestParameter::setIncludeContests);
        requestParameterSetter.put(PLAYERS_VALUE, tmpRequestParameter::setIncludePlayers);
    }

    public Pageable populatePageableRequestParameter(int page, int pageSize, ResourceObjectType resourceType) {
        return PageRequest.of(page, pageSize, Sort.by(pageableSortParameters.get(resourceType)));
    }

    public Set<ErrorDataDTO> populateRequestParameter(String key, String value,
                                                      RequestParameter finalRequestParameter) {
        List<String> allValues = Arrays.stream(StringUtils.split(StringUtils.defaultString(value), PARAMETER_SEPARATOR))
                .collect(Collectors.toList());
        Set<String> notAllowedValues = removeNotAllowedValues(key, allValues);

        tmpRequestParameter.resetStateToDefault();
        allValues.stream().map(requestParameterSetter::get).forEach(setter -> setter.accept(true));
        tmpRequestParameter.copyRequestParameterTo(finalRequestParameter);
        return createWarningsFromNotAllowedParameters(key, notAllowedValues);
    }

    private Set<String> removeNotAllowedValues(String key, List<String> values) {
        Set<String> notAllowedValues = values.stream().filter(value -> !allowedParameters.get(key).contains(value))
                .collect(Collectors.toSet());

        values.removeAll(notAllowedValues);
        return notAllowedValues;
    }

    private Set<ErrorDataDTO> createWarningsFromNotAllowedParameters(String key, Set<String> notAllowedValues) {
        return notAllowedValues.stream()
                .map(notAllowedValue -> ErrorDataDTO.builder()
                        .errorConstant(REQUEST_PARAMETER_NOT_ALLOWED)
                        .detailParameter(notAllowedValue)
                        .pointer(PARAMETER_URL_SEPARATOR + key).build())
                .collect(Collectors.toSet());
    }
}
