package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.db.v1.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

@Getter
@Setter
@AllArgsConstructor
public class ContestToDtoConverter implements Converter<Contest, ContestDTO> {

    private ModelMapper modelMapper;

    @Override
    public ContestDTO convert(MappingContext<Contest, ContestDTO> context) {
        Contest sourceContest = context.getSource();
        ContestDTO destinationContest = context.getDestination();

        modelMapper.map(sourceContest.getScore(), destinationContest);
        return destinationContest;
    }
}