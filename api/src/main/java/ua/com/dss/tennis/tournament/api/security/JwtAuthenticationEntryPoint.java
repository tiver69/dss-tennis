package ua.com.dss.tennis.tournament.api.security;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.exception.handler.MainExceptionHandler;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData.ErrorDataSource;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.UNAUTHORIZED;

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
