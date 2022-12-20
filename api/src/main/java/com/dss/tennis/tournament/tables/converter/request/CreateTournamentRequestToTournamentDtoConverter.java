package com.dss.tennis.tournament.tables.converter.request;

import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentAttributes;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.CreteTournamentRequest;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

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