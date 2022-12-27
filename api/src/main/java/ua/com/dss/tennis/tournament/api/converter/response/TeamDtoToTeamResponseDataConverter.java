package ua.com.dss.tennis.tournament.api.converter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;
import ua.com.dss.tennis.tournament.api.model.definitions.team.TeamResponse.TeamResponseData;
import ua.com.dss.tennis.tournament.api.model.definitions.team.TeamResponse.TeamResponseRelationships;
import ua.com.dss.tennis.tournament.api.model.dto.TeamDTO;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.PLAYER;
import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.TEAM;

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