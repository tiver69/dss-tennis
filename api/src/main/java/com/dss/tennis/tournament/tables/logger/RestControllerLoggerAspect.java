package com.dss.tennis.tournament.tables.logger;

import com.dss.tennis.tournament.tables.security.Admin;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;

@Aspect
@Component
public class RestControllerLoggerAspect {

    @Pointcut("within(com.dss.tennis.tournament.tables.controller..*)")
    public void controllerMethod() {
    }

    @Before("controllerMethod()")
    public void logBeforeRequestMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] params = signature.getMethod().getParameters();
        String pathParams = params.length == 0 ? "" : getPathParameters(params, joinPoint.getArgs());

        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info(getRequestedByUsername() + "METHOD STARTED  ||  {}, {}  ||", signature.getMethod()
                .getName(), pathParams);
    }

    @AfterReturning(pointcut = "controllerMethod()", returning = "entity")
    public void logMethodAfterReturn(JoinPoint joinPoint, ResponseEntity<?> entity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] params = signature.getMethod().getParameters();
        String pathParams = params.length == 0 ? "" : getPathParameters(params, joinPoint.getArgs());

        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info(getRequestedByUsername() + "METHOD ENDED    ||  {}, {}  ||  Status code {}", signature.getMethod()
                .getName(), pathParams, entity.getStatusCodeValue());
    }


    @AfterThrowing(pointcut = "controllerMethod()", throwing = "exception")
    public void logMethodAfterThrow(JoinPoint joinPoint, RuntimeException exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] params = signature.getMethod().getParameters();
        String pathParams = params.length == 0 ? "" : getPathParameters(params, joinPoint.getArgs());

        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.info(getRequestedByUsername() + "METHOD ENDED    ||  {}, {}  ||  With {} exception: {}", signature
                .getMethod().getName(), pathParams, exception.getClass().getName(), exception.getMessage());
        exception.printStackTrace();
    }

    private String getPathParameters(Parameter[] params, Object[] args) {
        StringBuilder pathParamsBuilder = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            if (params[i].getAnnotation(PathVariable.class) != null || params[i]
                    .getAnnotation(RequestParam.class) != null) {
                pathParamsBuilder.append(", ");
                pathParamsBuilder.append(params[i].getName()).append(" = ").append(args[i]);
            }
        }
        return pathParamsBuilder.toString().replaceFirst(", ", "");
    }

    private String getRequestedByUsername() {
        Object requestedBy = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = requestedBy instanceof Admin ? ((Admin) requestedBy).getUsername() : "anonymous";
        return String.format("%-12s: ", "[" + username + "] ");
    }
}
