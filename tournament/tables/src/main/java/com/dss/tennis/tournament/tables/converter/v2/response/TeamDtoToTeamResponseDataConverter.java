package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseRelationships;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;
import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TEAM;

@Getter
@Setter
@AllArgsConstructor
public class TeamDtoToTeamResponseDataConverter implements Converter<TeamDTO, TeamResponseData> {

    private ModelMapper modelMapper;

    @Override
    public TeamResponseData convert(MappingContext<TeamDTO, TeamResponseData> context) {
        TeamDTO teamDto = context.getSource();

        TeamResponseRelationships teamRelationships = new TeamResponseRelationships(
                new SimpleResourceObject(teamDto.getPlayerOne().getId(), PLAYER.value),
                new SimpleResourceObject(teamDto.getPlayerTwo().getId(), PLAYER.value));

        return TeamResponseData.builder()
                .id(teamDto.getId())
                .relationships(teamRelationships)
                .links(Links.builder()
                        .self(String.format(TEAM.selfLinkFormat, teamDto.getId()))
                        .build())
                .build();
    }
}