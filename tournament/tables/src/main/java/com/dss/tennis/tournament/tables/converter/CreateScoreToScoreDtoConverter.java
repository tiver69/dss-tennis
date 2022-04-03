package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.request.CreateScore;
import com.dss.tennis.tournament.tables.model.request.CreateScore.SetScoreAttributes;
import com.dss.tennis.tournament.tables.model.response.v1.ResourceObject;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.*;

import static com.dss.tennis.tournament.tables.model.db.v2.SetType.*;

@Getter
@Setter
public class CreateScoreToScoreDtoConverter extends AbstractResourceObjectConverter implements Converter<CreateScore,
        ScoreDTO> {

    public CreateScoreToScoreDtoConverter(String allowedResourceType) {
        super(allowedResourceType);
    }

    @Override
    public ScoreDTO convert(MappingContext<CreateScore, ScoreDTO> context) {
        CreateScore createScore = context.getSource();
        Map<SetType, SetScoreDTO> sets = new HashMap<>();
        Set<ErrorDataDTO> errors = new HashSet<>();

        mapSetScoreDto(createScore.getSetOne(), SET_ONE, errors).ifPresent(setScore -> sets.put(SET_ONE, setScore));
        mapSetScoreDto(createScore.getSetTwo(), SET_TWO, errors).ifPresent(setScore -> sets.put(SET_TWO, setScore));
        mapSetScoreDto(createScore.getSetThree(), SET_THREE, errors)
                .ifPresent(setScore -> sets.put(SET_THREE, setScore));
        mapSetScoreDto(createScore.getTieBreak(), TIE_BREAK, errors)
                .ifPresent(setScore -> sets.put(TIE_BREAK, setScore));
        if (!errors.isEmpty()) throw new DetailedException(errors);
        return new ScoreDTO(sets);
    }

    private Optional<SetScoreDTO> mapSetScoreDto(ResourceObject<SetScoreAttributes> set, SetType type,
                                                 Set<ErrorDataDTO> errors) {
        if (set == null) return Optional.empty();
        errors.addAll(validateResourceObjectMappingWitAttributes(set, type.value));
        if (set.getAttributes() == null) return Optional.empty();
        return Optional.of(new SetScoreDTO(set.getId(), set.getAttributes().getParticipantOne(), set
                .getAttributes().getParticipantTwo()));
    }
}