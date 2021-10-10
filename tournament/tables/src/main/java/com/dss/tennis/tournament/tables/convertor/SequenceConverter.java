package com.dss.tennis.tournament.tables.convertor;

import com.dss.tennis.tournament.tables.model.dto.AbstractSequentialDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SequenceConverter<S, D extends AbstractSequentialDTO> extends AbstractConverter<List<S>,
        List<D>> {

    private ModelMapper modelMapper;
    private Class<D> destinationClass;

    @Override
    protected List<D> convert(List<S> source) {
        List<D> destination = new ArrayList<>();

        for (int i = 0; i < source.size(); i++) {
            destination.add(convertSequenceElement(source.get(i), i));
        }
        return destination;
    }

    private D convertSequenceElement(S sourceElement, int sequenceNumber) {
        D destinationElement = modelMapper.map(sourceElement, this.destinationClass);
        destinationElement.setSequenceNumber(sequenceNumber);

        return destinationElement;
    }
}
