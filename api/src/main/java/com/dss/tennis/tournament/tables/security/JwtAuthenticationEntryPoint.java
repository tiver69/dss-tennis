package com.dss.tennis.tournament.tables.security;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.exception.handler.MainExceptionHandler;
import com.dss.tennis.tournament.tables.model.definitions.ErrorResponse;
import com.dss.tennis.tournament.tables.model.definitions.ErrorResponse.ErrorData;
import com.dss.tennis.tournament.tables.model.definitions.ErrorResponse.ErrorData.ErrorDataSource;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.UNAUTHORIZED;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String UNAUTHORIZED_POINTER = "Authentication token";

    @Autowired
    private MainExceptionHandler mainExceptionHandler;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException exception) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);

        List<ErrorData> errorData = getErrorDataList(exception);
        String errorResponse = objectMapper.writeValueAsString(new ErrorResponse(errorData));
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(mainExceptionHandler.getHttpStatus(errorData).value());
        httpServletResponse.getWriter().print(errorResponse);
    }

    private List<ErrorData> getErrorDataList(AuthenticationException exception) {
        if (exception.getCause() instanceof DetailedException) {
            return ((DetailedException) exception.getCause()).getErrors().stream()
                    .map(mainExceptionHandler::createErrorData)
                    .collect(Collectors.toList());
        }

        ErrorData errorData = mainExceptionHandler.createErrorData(new ErrorDataDTO(UNAUTHORIZED));
        errorData.setSource(ErrorDataSource.builder().pointer(UNAUTHORIZED_POINTER).build());
        return List.of(errorData);
    }
}
