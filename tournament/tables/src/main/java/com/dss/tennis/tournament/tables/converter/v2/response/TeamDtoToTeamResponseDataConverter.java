package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamRelationships;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.List;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.PLAYER;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.TEAM;

@Getter
@Setter
@AllArgsConstructor
public class TeamDtoToTeamResponseDataConverter implements Converter<TeamDTO, TeamResponseData> {

    private ModelMapper modelMapper;

    @Override
    public TeamResponseData convert(MappingContext<TeamDTO, TeamResponseData> context) {
        TeamDTO teamDto = context.getSource();

        TeamRelationships teamRelationships = new TeamRelationships(
                List.of(new SimpleResourceObject(teamDto.getPlayerOne().getId(), PLAYER.value),
                        new SimpleResourceObject(teamDto.getPlayerTwo().getId(), PLAYER.value)));

        List<Object> teamIncluded = new ArrayList<>();
        teamIncluded.add(modelMapper.map(teamDto.getPlayerOne(), PlayerResponseData.class));
        teamIncluded.add(modelMapper.map(teamDto.getPlayerTwo(), PlayerResponseData.class));

        return TeamResponseData.builder()
                .id(teamDto.getId())
                .relationships(teamRelationships)
                .included(teamIncluded)
                .links(Links.builder()
                        .self(String.format(TEAM.selfLinkFormat, teamDto.getId()))
                        .build())
                .build();
    }
}