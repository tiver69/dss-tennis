package ua.com.dss.tennis.tournament.api.converter.response.pageable;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.PageableTournamentResponse;
import ua.com.dss.tennis.tournament.api.model.dto.PageableDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

public class PageableDtoToPageableTournamentResponse extends AbstractPageableDtoToPageableResponse<TournamentDTO,
        PageableTournamentResponse> implements Converter<PageableDTO, PageableTournamentResponse> {

    public PageableDtoToPageableTournamentResponse(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public PageableTournamentResponse convert(MappingContext<PageableDTO, PageableTournamentResponse> context) {
        PageableDTO<TournamentDTO> pageableTournamentDto = context.getSource();
        return convertPageableResponse(pageableTournamentDto, PageableTournamentResponse.class);
    }
}

