package com.dss.tennis.tournament.tables.converter.modelmapper;

import com.dss.tennis.tournament.tables.converter.ContestToDtoConverter;
import com.dss.tennis.tournament.tables.converter.PlayerDtoToResourceObjectConverter;
import com.dss.tennis.tournament.tables.converter.TournamentDtoToGetResponseConverter;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
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
                skip(destination.getContests());
            }
        });
        modelMapper.getTypeMap(TournamentDTO.class, GetTournament.class)
                .setPostConverter(new TournamentDtoToGetResponseConverter(modelMapper));

        modelMapper.createTypeMap(Contest.class, ContestDTO.class)
                .setPostConverter(new ContestToDtoConverter(modelMapper));
        modelMapper.createTypeMap(PlayerDTO.class, ResourceObject.class)
                .setPostConverter(new PlayerDtoToResourceObjectConverter());
        return modelMapper;
    }
}
