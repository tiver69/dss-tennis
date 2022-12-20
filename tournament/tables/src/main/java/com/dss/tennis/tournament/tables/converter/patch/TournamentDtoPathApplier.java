package com.dss.tennis.tournament.tables.converter.patch;

import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentPatch;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Setter
public class TournamentDtoPathApplier implements Converter<TournamentPatch, TournamentDTO> {

    @Override
    public TournamentDTO convert(MappingContext<TournamentPatch, TournamentDTO> context) {
        TournamentPatch patch = context.getSource();
        TournamentDTO tournament = context.getDestination();

        applyValue(patch.getName(), tournament::setName);
        applyValue(patch.getParticipantType(), tournament::setParticipantType);
        applyValue(patch.getTournamentType(), tournament::setTournamentType);
        applyValue(patch.getBeginningDate(), tournament::setBeginningDate);
        return tournament;
    }

    private <T> void applyValue(Optional<T> patchValue, Consumer<T> setFunction) {
        patchValue.ifPresent(setFunction);
    }
}