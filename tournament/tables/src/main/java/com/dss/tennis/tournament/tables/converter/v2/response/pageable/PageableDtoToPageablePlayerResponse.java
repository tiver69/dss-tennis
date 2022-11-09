package com.dss.tennis.tournament.tables.converter.v2.response.pageable;

import com.dss.tennis.tournament.tables.model.definitions.player.PageablePlayerResponse;
import com.dss.tennis.tournament.tables.model.dto.PageableDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class PageableDtoToPageablePlayerResponse extends AbstractPageableDtoToPageableResponse<PlayerDTO,
        PageablePlayerResponse> implements Converter<PageableDTO, PageablePlayerResponse> {

    public PageableDtoToPageablePlayerResponse(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public PageablePlayerResponse convert(MappingContext<PageableDTO, PageablePlayerResponse> context) {
        PageableDTO<PlayerDTO> pageablePlayerDto = context.getSource();
        return convertPageableResponse(pageablePlayerDto, PageablePlayerResponse.class);
    }
}

