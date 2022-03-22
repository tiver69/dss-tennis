package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.request.CreateScore;
import com.dss.tennis.tournament.tables.model.request.CreateScore.SetScoreAttributes;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class CreateScoreToScoreDtoConverter implements Converter<CreateScore, ScoreDTO> {

    @Override
    public ScoreDTO convert(MappingContext<CreateScore, ScoreDTO> context) {
        CreateScore createScore = context.getSource();
        Map<SetType, SetScoreDTO> sets = new HashMap<>();

        mapSetScoreDto(createScore.getSetOne()).ifPresent(setScore -> sets.put(SetType.SET_ONE, setScore));
        mapSetScoreDto(createScore.getSetTwo()).ifPresent(setScore -> sets.put(SetType.SET_TWO, setScore));
        mapSetScoreDto(createScore.getSetThree()).ifPresent(setScore -> sets.put(SetType.SET_THREE, setScore));
        mapSetScoreDto(createScore.getTieBreak()).ifPresent(setScore -> sets.put(SetType.TIE_BREAK, setScore));
        return new ScoreDTO(sets);
    }

    private Optional<SetScoreDTO> mapSetScoreDto(ResourceObject<SetScoreAttributes> set) {
        if (set == null) return Optional.empty();
        SetScoreDTO setScoreDto = set.getAttributes() == null ? new SetScoreDTO(null, null) :
                new SetScoreDTO(set.getAttributes().getParticipantOne(), set.getAttributes().getParticipantTwo());
        return Optional.of(setScoreDto);
    }
}