package com.dss.tennis.tournament.tables.converter.request;

import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentAttributes;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentPatch;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.UpdateTournamentRequest;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Optional;

@Getter
@Setter
public class UpdateTournamentRequestToPatchTournamentConverter implements Converter<UpdateTournamentRequest,
        TournamentPatch> {

    @Override
    public TournamentPatch convert(MappingContext<UpdateTournamentRequest, TournamentPatch> context) {
        UpdateTournamentRequest update = context.getSource();
        TournamentAttributes tournamentAttributes = update.getAttributes();

        TournamentPatch tournamentPatch = new TournamentPatch();
        tournamentPatch.setName(Optional.ofNullable(tournamentAttributes.getName()));
        tournamentPatch.setTournamentType(Optional.ofNullable(tournamentAttributes.getTournamentType()));
        tournamentPatch.setParticipantType(Optional.ofNullable(tournamentAttributes.getParticipantType()));
        tournamentPatch.setBeginningDate(Optional.ofNullable(tournamentAttributes.getBeginningDate()));
        return tournamentPatch;
    }
}