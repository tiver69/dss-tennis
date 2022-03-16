package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.converter.modelmapper.ModelMapperFactory;
import com.dss.tennis.tournament.tables.model.dto.AbstractSequentialDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ConverterHelper {

    @Autowired
    private ModelMapperFactory modelMapperFactory;

    public <S, D> D convert(S source, Class<D> destinationClass) {
        return convert(source, destinationClass, false);
    }

    public <S, D> D convert(S source, Class<D> destinationClass, boolean includeSequential) {
        Object destinationObject = modelMapperFactory.getCustomizedModelMapper().map(source, destinationClass);
        if (includeSequential) {
            addSequential(destinationObject);
        }
        return destinationClass.cast(destinationObject);
    }

    public <S, D> List<D> convert(Collection<S> sourceIterable, Class<D> destinationClass, boolean includeSequential) {
        List<D> destinationList = sourceIterable.stream()
                .map(source -> convert(source, destinationClass, includeSequential))
                .collect(Collectors.toList());
        if (includeSequential && AbstractSequentialDTO.class.isAssignableFrom(destinationClass))
            addSequentialToList((List<? extends AbstractSequentialDTO>) destinationList);
        return destinationList;
    }

    private <D> void addSequential(D destinationObject) {
        List<String> sequentialFieldNames = Arrays.stream(destinationObject.getClass().getDeclaredFields())
                .map(this::getFieldNameIfSequential)
                .filter(Objects::nonNull).collect(Collectors.toList());
        for (String fieldName : sequentialFieldNames) {
            List<?> sequentialList = getSequentialValue(fieldName, destinationObject);
            if (sequentialList == null || sequentialList.isEmpty()) continue;
            if (!(sequentialList.get(0) instanceof AbstractSequentialDTO)) continue;
            addSequentialToList((List<? extends AbstractSequentialDTO>) sequentialList);
        }
    }

    private String getFieldNameIfSequential(Field field) {
        return List.class.isAssignableFrom(field.getType()) ? field.getName() : null;
    }

    private List<?> getSequentialValue(String fieldName, Object destinationObject) {
        try {
            Method method = destinationObject.getClass()
                    .getMethod("get" + StringUtils.capitalize(fieldName), null);
            return (List<?>) method.invoke(destinationObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Something went wrong during sequential conversion.");
        }
    }

    private void addSequentialToList(List<? extends AbstractSequentialDTO> destinationList) {
        for (byte i = 0; i < destinationList.size(); i++) {
            destinationList.get(i).setSequenceNumber(i);
        }
    }
}
