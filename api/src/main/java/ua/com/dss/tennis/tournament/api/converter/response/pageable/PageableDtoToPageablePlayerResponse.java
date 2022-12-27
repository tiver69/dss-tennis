package ua.com.dss.tennis.tournament.api.converter.response.pageable;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.player.PageablePlayerResponse;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;

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

