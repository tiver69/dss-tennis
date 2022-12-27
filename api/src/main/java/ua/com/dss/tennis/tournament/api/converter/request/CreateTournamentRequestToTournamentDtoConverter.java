package ua.com.dss.tennis.tournament.api.converter.request;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentAttributes;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentRequest.CreteTournamentRequest;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

@Getter
@Setter
public class CreateTournamentRequestToTournamentDtoConverter implements Converter<CreteTournamentRequest,
        TournamentDTO> {

    @Override
    public TournamentDTO convert(MappingContext<CreteTournamentRequest, TournamentDTO> context) {
        CreteTournamentRequest createTournament = context.getSource();
        TournamentAttributes tournamentAttributes = createTournament.getAttributes();

        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setName(tournamentAttributes.getName());
        tournamentDTO.setTournamentType(tournamentAttributes.getTournamentType());
        tournamentDTO.setBeginningDate(tournamentAttributes.getBeginningDate());
        tournamentDTO.setParticipantType(tournamentAttributes.getParticipantType());
        return tournamentDTO;
    }
}