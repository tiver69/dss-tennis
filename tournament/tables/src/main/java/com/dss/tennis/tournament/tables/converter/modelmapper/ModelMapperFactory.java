package com.dss.tennis.tournament.tables.converter.modelmapper;

import com.dss.tennis.tournament.tables.converter.ContestDtoToGetResponseConverter;
import com.dss.tennis.tournament.tables.converter.ContestToDtoConverter;
import com.dss.tennis.tournament.tables.model.db.v1.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperFactory {

    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    public ModelMapper getCustomizedModelMapper() {
        ModelMapper modelMapper = getModelMapper();
        modelMapper.createTypeMap(Contest.class, ContestDTO.class)
                .setPostConverter(new ContestToDtoConverter(modelMapper));
        modelMapper.createTypeMap(ContestDTO.class, GetContest.class)
                .setPostConverter(new ContestDtoToGetResponseConverter());
        return modelMapper;
    }
}
