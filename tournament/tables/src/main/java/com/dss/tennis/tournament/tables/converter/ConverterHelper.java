package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.converter.modelmapper.ModelMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConverterHelper {

    @Autowired
    private ModelMapperFactory modelMapperFactory;

    public <S, D> D convert(S source, Class<D> destinationClass) {
        return convert(source, destinationClass, null);
    }

    public <S, D> D convert(S source, Class<D> destinationClass, String extraString) {
        Object destinationObject = modelMapperFactory.getCustomizedModelMapper(extraString)
                .map(source, destinationClass);

        return destinationClass.cast(destinationObject);
    }
}
