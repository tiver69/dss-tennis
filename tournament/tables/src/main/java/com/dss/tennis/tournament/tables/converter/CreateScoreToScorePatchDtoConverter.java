package com.dss.tennis.tournament.tables.converter;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.dto.ErrorDataDTO;
import com.dss.tennis.tournament.tables.model.dto.ScorePatchDTO;
import com.dss.tennis.tournament.tables.model.dto.ScorePatchDTO.SetScorePatchDTO;
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
public class CreateScoreToScorePatchDtoConverter extends AbstractResourceObjectConverter implements Converter<CreateScore,
        ScorePatchDTO> {

    public CreateScoreToScorePatchDtoConverter(String allowedResourceType) {
        super(allowedResourceType);
    }

    @Override
    public ScorePatchDTO convert(MappingContext<CreateScore, ScorePatchDTO> context) {
        CreateScore createScore = context.getSource();
        Map<SetType, SetScorePatchDTO> sets = new HashMap<>();
        Set<ErrorDataDTO> errors = new HashSet<>();

        mapSetScoreDto(createScore.getSetOne(), SET_ONE, errors).ifPresent(setScore -> sets.put(SET_ONE, setScore));
        mapSetScoreDto(createScore.getSetTwo(), SET_TWO, errors).ifPresent(setScore -> sets.put(SET_TWO, setScore));
        mapSetScoreDto(createScore.getSetThree(), SET_THREE, errors)
                .ifPresent(setScore -> sets.put(SET_THREE, setScore));
        mapSetScoreDto(createScore.getTieBreak(), TIE_BREAK, errors)
                .ifPresent(setScore -> sets.put(TIE_BREAK, setScore));
        if (!errors.isEmpty()) throw new DetailedException(errors);
        return new ScorePatchDTO(sets);
    }

    private Optional<SetScorePatchDTO> mapSetScoreDto(ResourceObject<SetScoreAttributes> set, SetType type,
                                                 Set<ErrorDataDTO> errors) {
        if (set == null) return Optional.empty();
        errors.addAll(validateResponseObjectMappingWitAttributes(set, type.value));
        if (set.getAttributes() == null) return Optional.empty();
        return Optional.of(new SetScorePatchDTO(set.getId(), set.getAttributes().getParticipantOne(), set
                .getAttributes().getParticipantTwo()));
    }
}