package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.player.PageablePlayerResponse;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.PLAYER;

@Getter
@Setter
public class PageableDtoToPageablePlayerResponse implements Converter<PageableDTO, PageablePlayerResponse> {

    private ModelMapper modelMapper;

    public PageableDtoToPageablePlayerResponse(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PageablePlayerResponse convert(MappingContext<PageableDTO, PageablePlayerResponse> context) {
        PageableDTO<PlayerDTO> pageablePlayersDto = context.getSource();
        int pageSize = pageablePlayersDto.getPageSize();
        int currentPage = pageablePlayersDto.getCurrentPage();

        return PageablePlayerResponse.builder()
                .totalPages(pageablePlayersDto.getTotalPages())
                .page(pageablePlayersDto.getPage().stream().map(playerDto -> modelMapper.map(playerDto,
                        PlayerResponseData.class))
                        .collect(Collectors.toList()))
                .links(Links.builder()
                        .first(String.format(PLAYER.pageableLinkFormat, 1, pageSize))
                        .last(String.format(PLAYER.pageableLinkFormat, pageablePlayersDto.getTotalPages(), pageSize))
                        .prev(String
                                .format(PLAYER.pageableLinkFormat, currentPage - 1, pageSize))
                        .self(String.format(PLAYER.pageableLinkFormat, currentPage, pageSize))
                        .next(String
                                .format(PLAYER.pageableLinkFormat, currentPage + 1, pageSize)) //todo: ignore next if current is last
                        .build())
                .build();
    }
}