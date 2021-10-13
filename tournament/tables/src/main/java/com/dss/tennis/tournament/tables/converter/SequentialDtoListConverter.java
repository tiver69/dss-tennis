package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.AbstractSequentialDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Deprecated
public class SequentialDtoListConverter implements Converter<ArrayList, ArrayList> {

    @Override
    public ArrayList<?> convert(MappingContext<ArrayList, ArrayList> mappingContext) {
        ArrayList<?> destinationList = mappingContext.getDestination();

        if (isSequentialConverterApplicable(destinationList)) {
            for (int i = 0; i < mappingContext.getSource().size(); i++) {
                ((AbstractSequentialDTO) destinationList.get(i)).setSequenceNumber(i);
            }
        }
        return destinationList;
    }

    private boolean isSequentialConverterApplicable(List<?> destinationList) {
        return destinationList != null && !destinationList.isEmpty() && destinationList
                .get(0) instanceof AbstractSequentialDTO;
    }
}
