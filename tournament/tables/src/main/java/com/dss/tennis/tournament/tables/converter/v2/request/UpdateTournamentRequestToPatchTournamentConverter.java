package com.dss.tennis.tournament.tables.converter.v2.request;

import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentAttributes;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentRequest.UpdateTournamentRequest;
import com.dss.tennis.tournament.tables.model.request.PatchTournament;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Optional;

@Getter
@Setter
public class UpdateTournamentRequestToPatchTournamentConverter implements Converter<UpdateTournamentRequest,
        PatchTournament> {

    @Override
    public PatchTournament convert(MappingContext<UpdateTournamentRequest, PatchTournament> context) {
        UpdateTournamentRequest update = context.getSource();
        TournamentAttributes tournamentAttributes = update.getAttributes();

        PatchTournament patchTournament = new PatchTournament();
        patchTournament.setName(Optional.ofNullable(tournamentAttributes.getName()));
        patchTournament.setTournamentType(Optional.ofNullable(tournamentAttributes.getTournamentType()));
        patchTournament.setParticipantType(Optional.ofNullable(tournamentAttributes.getParticipantType()));
        patchTournament.setBeginningDate(Optional.ofNullable(tournamentAttributes.getBeginningDate()));
        //todo: business logic with status
        return patchTournament;
    }
}