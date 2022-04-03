package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import com.dss.tennis.tournament.tables.model.response.v1.GetPlayer;
import com.dss.tennis.tournament.tables.model.response.v1.GetTeam;
import com.dss.tennis.tournament.tables.model.response.v1.GetTeam.GetTeamAttributes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

@Getter
@Setter
@AllArgsConstructor
public class TeamDtoToGetTeamConverter implements Converter<TeamDTO, GetTeam> {

    private ModelMapper modelMapper;

    @Override
    public GetTeam convert(MappingContext<TeamDTO, GetTeam> context) {
        TeamDTO teamDto = context.getSource();
        GetTeam resourceObject = context.getDestination();

        GetTeamAttributes attributes = new GetTeamAttributes();
        attributes.setPlayerOne(modelMapper.map(teamDto.getPlayerOne(), GetPlayer.class));
        attributes.setPlayerTwo(modelMapper.map(teamDto.getPlayerTwo(), GetPlayer.class));
        resourceObject.setAttributes(attributes);
        return resourceObject;
    }
}