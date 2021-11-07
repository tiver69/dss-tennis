package com.dss.tennis.tournament.tables.helper;

import com.dss.tennis.tournament.tables.exception.handler.WarningHandler;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.response.v1.ErrorData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.error.WarningConstant.REQUEST_PARAMETER_NOT_ALLOWED;

@Component
public class RequestParameterHelper {

    public static String PARAMETER_URL_SEPARATOR = "?";
    public static String PARAMETER_SEPARATOR = ",";
    public static String INCLUDE_KEY = "include";
    public static String CONTEST_VALUE = "contests";

    private final Map<String, List<String>> allowedParameters = new HashMap<>();
    private final Map<String, Consumer<Boolean>> requestParameterSetter = new HashMap<>();
    private RequestParameter tmpRequestParameter;

    @Autowired
    private WarningHandler warningHandler;

    @PostConstruct
    protected void initialize() {
        allowedParameters.put(INCLUDE_KEY, List.of(CONTEST_VALUE));

        tmpRequestParameter = new RequestParameter();
        requestParameterSetter.put(CONTEST_VALUE, tmpRequestParameter::setIncludeContests);
    }

    public List<ErrorData> populateRequestParameter(String key, String value, RequestParameter finalRequestParameter) {
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

    private List<ErrorData> createWarningsFromNotAllowedParameters(String key, Set<String> notAllowedValues) {
        return notAllowedValues.stream()
                .map(notAllowedValue -> warningHandler.createWarning(REQUEST_PARAMETER_NOT_ALLOWED, notAllowedValue))
                .peek(errorData -> errorData.getSource().setPointer(PARAMETER_URL_SEPARATOR + key))
                .collect(Collectors.toList());
    }
}
