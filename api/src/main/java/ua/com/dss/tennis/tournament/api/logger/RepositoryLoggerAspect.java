package ua.com.dss.tennis.tournament.api.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord;
import ua.com.dss.tennis.tournament.api.logger.anotation.RepositoryLogRecord.QueryMethod;

import javax.annotation.PostConstruct;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RepositoryLoggerAspect {

    private final Map<String, String> repositoryToEntityName = new HashMap<>();

    @PostConstruct
    public void initConfigurations() {
        repositoryToEntityName.put("ua.com.dss.tennis.tournament.api.repository.ContestRepository", "CONTEST");
        repositoryToEntityName.put("ua.com.dss.tennis.tournament.api.repository.ScoreRepository", "SCORE");
        repositoryToEntityName.put("ua.com.dss.tennis.tournament.api.repository.PlayerRepository", "PLAYER");
        repositoryToEntityName.put("ua.com.dss.tennis.tournament.api.repository.TeamRepository", "TEAM");
        repositoryToEntityName.put("ua.com.dss.tennis.tournament.api.repository.TournamentRepository", "TOURNAMENT");
    }

    @Pointcut("execution(* ua.com.dss.tennis.tournament.api.repository.*.*(..))")
    public void repositoryMethod() {
    }

    @Before("repositoryMethod()")
    public void logBefore(JoinPoint joinPoint) {
        Class targetClass = joinPoint.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(targetClass);
        if (logger.isDebugEnabled()) return;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RepositoryLogRecord recordAnnotation = signature.getMethod().getAnnotation(RepositoryLogRecord.class);

        String entityName = repositoryToEntityName.get(targetClass.getName());
        String recordFormat = recordAnnotation.method().recordFormat;

        String queryParams = getQueryParams(signature.getMethod().getParameters(), joinPoint.getArgs());
        if (recordAnnotation.method() == QueryMethod.IS_QUERY)
            queryParams = signature.getMethod().getName() + queryParams;

        logger.debug(String
                .format(recordFormat, entityName, queryParams));
    }

    @AfterReturning(pointcut = "repositoryMethod()", returning = "resultObject")
    public void logMethodAfterReturn(JoinPoint joinPoint, Object resultObject) {
        Class targetClass = joinPoint.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(targetClass);
        if (!logger.isDebugEnabled()) return;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RepositoryLogRecord recordAnnotation = signature.getMethod().getAnnotation(RepositoryLogRecord.class);

        String entityName = repositoryToEntityName.get(targetClass.getName());
        String recordFormat = recordAnnotation.method().recordFormat;

        String queryParams = getQueryParams(signature.getMethod().getParameters(), joinPoint.getArgs());
        if (recordAnnotation.method() == QueryMethod.IS_QUERY)
            queryParams = signature.getMethod().getName() + "? " + queryParams;

        logger.debug(String
                .format(recordFormat, entityName, queryParams) + getResultRecord(resultObject, recordAnnotation));
    }

    private String getQueryParams(Parameter[] params, Object[] args) {
        StringBuilder pathParamsBuilder = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            pathParamsBuilder.append(", ");
            if (params[i].getType() == Pageable.class) {
                Pageable pageArg = (Pageable) args[i];
                pathParamsBuilder.append("pageNumber = ").append(pageArg.getPageNumber())
                        .append(", pageSize = ").append(pageArg.getPageSize());
            } else {
                pathParamsBuilder.append(params[i].getName()).append(" = ").append(args[i]);
            }
        }
        return pathParamsBuilder.toString().replaceFirst(", ", "");
    }

    private String getResultRecord(Object resultObject, RepositoryLogRecord recordAnnotation) {
        String resultFormat = recordAnnotation.method() == QueryMethod.GET ? recordAnnotation
                .resultType().resultFormat : recordAnnotation.method().resultFormat;

        switch (recordAnnotation.resultType()) {
            case MULTIPLE_RECORDS:
                return String.format(resultFormat, ((Collection) resultObject).size());
            case PAGE:
                Page resultPage = (Page) resultObject;
                return String.format(resultFormat, resultPage.getNumber(), resultPage.getTotalPages() - 1, resultPage
                        .getContent().size());
            default:
                return String.format(resultFormat, resultObject);
        }
    }
}
