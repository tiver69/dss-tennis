package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.team.PageableTeamResponse;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseRelationships;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.TeamDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.PLAYER;
import static com.dss.tennis.tournament.tables.model.definitions.ResourceObjectType.TEAM;

@Getter
@Setter
public class PageableDtoToPageableTeamResponse implements Converter<PageableDTO, PageableTeamResponse> {

    private ModelMapper modelMapper;

    public PageableDtoToPageableTeamResponse(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PageableTeamResponse convert(MappingContext<PageableDTO, PageableTeamResponse> context) {
        PageableDTO<TeamDTO> pageableTeamsDto = context.getSource();
        int pageSize = pageableTeamsDto.getPageSize();
        int currentPage = pageableTeamsDto.getCurrentPage();

        return PageableTeamResponse.builder()
//                .totalPages(pageableTeamsDto.getTotalPages())
//                .page(
//                        pageableTeamsDto.getPage().stream()
//                                .map(this::convertTeamResponseDataWithoutIncluded)
//                                .collect(Collectors.toList())
//                )
//                .included(
//                        pageableTeamsDto.getPage().stream()
//                                .flatMap(teamDto -> Stream.of(teamDto.getPlayerOne(), teamDto.getPlayerTwo()))
//                                .distinct()
//                                .map(playerDto -> modelMapper.map(playerDto, PlayerResponseData.class))
//                                .collect(Collectors.toList())
//                )
//                .links(Links.builder()
//                        .first(String.format(TEAM.pageableLinkFormat, 1, pageSize))
//                        .last(String.format(TEAM.pageableLinkFormat, pageableTeamsDto.getTotalPages(), pageSize))
//                        .prev(String
//                                .format(TEAM.pageableLinkFormat, currentPage - 1, pageSize))
//                        .self(String.format(TEAM.pageableLinkFormat, currentPage, pageSize))
//                        .next(String
//                                .format(TEAM.pageableLinkFormat, currentPage + 1, pageSize)) //todo: ignore next if
//                                 current is last
//                        .build())
                .build();
    }

    private TeamResponseData convertTeamResponseDataWithoutIncluded(TeamDTO teamDto) {
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