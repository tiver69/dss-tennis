package com.dss.tennis.tournament.tables.converter.modelmapper;

import com.dss.tennis.tournament.tables.converter.*;
import com.dss.tennis.tournament.tables.model.dto.*;
import com.dss.tennis.tournament.tables.model.request.CreateScore;
import com.dss.tennis.tournament.tables.model.response.v1.*;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType;
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
        modelMapper.createTypeMap(PlayerDTO.class, ResourceObject.class)
                .setPostConverter(new PlayerDtoToResourceObjectConverter());
        modelMapper.createTypeMap(PlayerDTO.class, GetPlayer.class)
                .setPostConverter(new PlayerDtoToGetPlayerConverter());
        modelMapper.createTypeMap(PageableDTO.class, GetPageable.class)
                .setPostConverter(new PageableDtoToGetPageableConverter(modelMapper));
        modelMapper.createTypeMap(String.class, ResourceObjectType.class)
                .setConverter(new ResourceObjectTypeStringToEnumConverter());
        modelMapper.createTypeMap(CreateScore.class, ScoreDTO.class)
                .setConverter(new CreateScoreToScoreDtoConverter(ResourceObjectType.SET_SCORE.value));
        modelMapper.createTypeMap(CreateScore.class, ScorePatchDTO.class)
                .setConverter(new CreateScoreToScorePatchDtoConverter(ResourceObjectType.SET_SCORE.value));
        return modelMapper;
    }
}
