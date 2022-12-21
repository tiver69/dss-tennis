package com.dss.tennis.tournament.tables.validator.aop;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.model.definitions.team.CreateTeamRequest;
import com.dss.tennis.tournament.tables.validator.aop.anotation.PatchIdValidator;
import com.dss.tennis.tournament.tables.validator.aop.anotation.TeamCreationBeforeValidator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.RESOURCE_OBJECT_ID_NOT_CORRESPOND_TO_REQUEST;
import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.TEAM_PLAYER_REDUNDANT;

@Aspect
@Component
public class RestControllerValidatorAspect {

    private final Map<Class<? extends Annotation>, BiConsumer<JoinPoint, Annotation>> annotationToValidatorName =
            new HashMap<>();

    @PostConstruct
    public void initConfigurations() {
        annotationToValidatorName.put(PatchIdValidator.class, validatePatchId);
        annotationToValidatorName.put(TeamCreationBeforeValidator.class, validateTeamCreation);
    }

    @Pointcut("within(com.dss.tennis.tournament.tables.controller..*)")
    public void controllerMethod() {
    }

    @Before("controllerMethod()")
    public void validateBeforeRequestMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Arrays.stream(signature.getMethod().getAnnotations())
                .filter(an -> annotationToValidatorName.containsKey(an.annotationType()))
                .forEach(an -> annotationToValidatorName.get(an.annotationType()).accept(joinPoint, an));
    }

    private final BiConsumer<JoinPoint, Annotation> validatePatchId = (joinPoint, validatorAnnotation) -> {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        PatchIdValidator annotation = (PatchIdValidator) validatorAnnotation;
        Integer pathIdParameterValue = getPathIdParameter(annotation.pathIdParameterName(), signature
                .getMethod().getParameters(), joinPoint.getArgs());
        Integer bodyIdParameter = getBodyIdParameter(signature.getMethod().getParameters(), joinPoint
                .getArgs());
        if (!pathIdParameterValue.equals(bodyIdParameter))
            throw new DetailedException(RESOURCE_OBJECT_ID_NOT_CORRESPOND_TO_REQUEST, bodyIdParameter);
    };

    private final BiConsumer<JoinPoint, Annotation> validateTeamCreation = (joinPoint, validatorAnnotation) -> {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CreateTeamRequest requestBody = (CreateTeamRequest) getRequestBodyParameter(signature.getMethod()
                .getParameters(), joinPoint.getArgs());

        if (requestBody.getRelationships() != null && requestBody.getRelationships().getPlayers().size() > 2)
            throw new DetailedException(TEAM_PLAYER_REDUNDANT, null, (byte) requestBody.getRelationships().getPlayers()
                    .size());
    };

    private Integer getPathIdParameter(String idParameterName, Parameter[] params, Object[] args) {
        for (int i = 0; i < params.length; i++) {
            if (params[i].getAnnotation(PathVariable.class) != null && params[i].getName().equals(idParameterName)) {
                return (Integer) args[i];
            }
        }
        throw new IllegalArgumentException("No path Id-parameter with name" + idParameterName + " was found");
    }

    private Integer getBodyIdParameter(Parameter[] params, Object[] args) {
        for (int i = 0; i < params.length; i++) {
            if (params[i].getAnnotation(RequestBody.class) != null) {
                try {
                    Method getIdMethod = args[i].getClass().getMethod("getId");
                    return (Integer) getIdMethod.invoke(args[i]);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException("No Id-parameter was found in request body model");
                }
            }
        }
        throw new IllegalArgumentException("No request body was found for method");
    }

    private Object getRequestBodyParameter(Parameter[] params, Object[] args) {
        for (int i = 0; i < params.length; i++) {
            if (params[i].getAnnotation(RequestBody.class) != null)
                return args[i];
        }
        throw new IllegalArgumentException("No request body was found for method");
    }
}
