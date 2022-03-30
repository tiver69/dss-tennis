package com.dss.tennis.tournament.tables.converter.patch;

import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import com.dss.tennis.tournament.tables.model.request.PatchTournament;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Setter
public class TournamentDtoPathApplier implements Converter<PatchTournament, TournamentDTO> {

    @Override
    public TournamentDTO convert(MappingContext<PatchTournament, TournamentDTO> context) {
        PatchTournament patch = context.getSource();
        TournamentDTO tournament = context.getDestination();

        applyValue(patch.getName(), tournament::setName);
        applyValue(patch.getParticipantType(), tournament::setParticipantType);
        applyValue(patch.getTournamentType(), tournament::setTournamentType);
        applyValue(patch.getBeginningDate(), tournament::setBeginningDate);
        return tournament;
    }

    private <T> void applyValue(Optional<T> patchValue, Consumer<T> setFunction) {
        if (patchValue == null) return;
        if (!patchValue.isPresent()) setFunction.accept(null);
        else patchValue.ifPresent(setFunction);
    }
}