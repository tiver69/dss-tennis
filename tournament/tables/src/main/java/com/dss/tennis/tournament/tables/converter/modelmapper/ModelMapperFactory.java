package com.dss.tennis.tournament.tables.converter.modelmapper;

import com.dss.tennis.tournament.tables.converter.DoubleContestDtoToGetResponseConverter;
import com.dss.tennis.tournament.tables.converter.SingleContestDtoToGetResponseConverter;
import com.dss.tennis.tournament.tables.converter.ContestToDtoConverter;
import com.dss.tennis.tournament.tables.converter.PlayerDtoToResourceObjectConverter;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.response.v1.GetContest;
import com.dss.tennis.tournament.tables.model.response.v1.GetTournament;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperFactory {

    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    public ModelMapper getCustomizedModelMapper() {
        ModelMapper modelMapper = getModelMapper();

        modelMapper.addMappings(new PropertyMap<TournamentDTO, GetTournament>() {
            @Override
            protected void configure() {
                skip(destination.getType());
            }
        });

        modelMapper.createTypeMap(SingleContestDTO.class, GetContest.class)
                .setPostConverter(new SingleContestDtoToGetResponseConverter());
        modelMapper.createTypeMap(DoubleContestDTO.class, GetContest.class)
                .setPostConverter(new DoubleContestDtoToGetResponseConverter());
        modelMapper.createTypeMap(Contest.class, ContestDTO.class)
                .setPostConverter(new ContestToDtoConverter(modelMapper));
        modelMapper.createTypeMap(PlayerDTO.class, ResourceObject.class)
                .setPostConverter(new PlayerDtoToResourceObjectConverter());
        return modelMapper;
    }
}
