package com.dss.tennis.tournament.tables.logger;

import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord;
import com.dss.tennis.tournament.tables.logger.anotation.RepositoryLogRecord.QueryMethod;
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
        repositoryToEntityName.put("com.dss.tennis.tournament.tables.repository.ContestRepository", "CONTEST");
        repositoryToEntityName.put("com.dss.tennis.tournament.tables.repository.ScoreRepository", "SCORE");
        repositoryToEntityName.put("com.dss.tennis.tournament.tables.repository.PlayerRepository", "PLAYER");
        repositoryToEntityName.put("com.dss.tennis.tournament.tables.repository.SetScoreRepository", "SETS_SCORE");
        repositoryToEntityName.put("com.dss.tennis.tournament.tables.repository.TeamRepository", "TEAM");
        repositoryToEntityName.put("com.dss.tennis.tournament.tables.repository.TournamentRepository", "TOURNAMENT");
    }

    @Pointcut("execution(* com.dss.tennis.tournament.tables.repository.*.*(..))")
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
