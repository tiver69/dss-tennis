package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class ResourceObjectTypeStringToEnumConverter implements Converter<String, ResourceObjectType> {

    @Override
    public ResourceObjectType convert(MappingContext<String, ResourceObjectType> context) {
        String sourceValue = context.getSource();
        return ResourceObjectType.fromStringValue(sourceValue);
    }
}