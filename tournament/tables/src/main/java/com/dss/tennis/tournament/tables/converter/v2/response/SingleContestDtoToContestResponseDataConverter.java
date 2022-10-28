package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes.ContestAttributesScore;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestAttributes.ContestAttributesSetScore;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestRelationships;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestResponse.ContestResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.TechDefeat;
import com.dss.tennis.tournament.tables.model.definitions.player.PlayerResponse.PlayerResponseData;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.Map;

import static com.dss.tennis.tournament.tables.model.db.v2.SetType.*;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.CONTEST;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.PLAYER;

@Getter
@Setter
@AllArgsConstructor
public class SingleContestDtoToContestResponseDataConverter implements Converter<SingleContestDTO,
        ContestResponseData> {

    private ModelMapper modelMapper;

    @Override
    public ContestResponseData convert(MappingContext<SingleContestDTO, ContestResponseData> context) {
        SingleContestDTO contestDTO = context.getSource();

        return ContestResponseData.builder()
                .id(contestDTO.getId())
                .attributes(convertContestAttributes(contestDTO))
                .relationships(convertContestRelationships(contestDTO))
                .included(convertContestIncluded(contestDTO))
                .links(Links.builder()
                        .self(String.format(CONTEST.selfLinkFormat, 1, contestDTO.getId())) //todo: tournamentID here
                        .build())
                .build();
    }

    private ContestAttributes convertContestAttributes(ContestDTO contestDTO) {
        return ContestAttributes.builder()
                .techDefeat(convertTechDefeat(contestDTO))
                .score(convertScore(contestDTO))
                .build();
    }

    private TechDefeat convertTechDefeat(ContestDTO contestDTO) {
        if (!contestDTO.isTechDefeat()) return new TechDefeat(false, false);
        else if (contestDTO.getWinnerId() == null) return new TechDefeat(true, true);
        else if (contestDTO.getWinnerId().equals(contestDTO.participantOneId())) return new TechDefeat(false, true);
        return new TechDefeat(true, false);
    }

    private ContestAttributesScore convertScore(ContestDTO contestDTO) {
        Map<SetType, SetScoreDTO> sets = contestDTO.getScoreDto().getSets();
        return ContestAttributesScore.builder()
                .setOne(convertContestAttributesSetScore(sets.get(SET_ONE)))
                .setTwo(convertContestAttributesSetScore(sets.get(SET_TWO)))
                .setThree(convertContestAttributesSetScore(sets.get(SET_THREE)))
                .tieBreak(convertContestAttributesSetScore(sets.get(TIE_BREAK)))
                .build();
    }

    private ContestAttributesSetScore convertContestAttributesSetScore(SetScoreDTO set) {
        if (set == null) return null;
        return new ContestAttributesSetScore(set.getParticipantOneScore(), set.getParticipantTwoScore());
    }

    private ContestRelationships convertContestRelationships(SingleContestDTO contestDto) {
        SimpleResourceObject winner = contestDto.getWinnerId() == null ? null : new SimpleResourceObject(contestDto
                .getWinnerId(), PLAYER.value);
        return ContestRelationships.builder()
                .participantOne(new SimpleResourceObject(contestDto.participantOneId(), PLAYER.value))
                .participantTwo(new SimpleResourceObject(contestDto.participantTwoId(), PLAYER.value))
                .winner(winner)
                .build();
    }

    private List<Object> convertContestIncluded(SingleContestDTO contestDto) {
        return List.of(
                modelMapper.map(contestDto.getPlayerOne(), PlayerResponseData.class),
                modelMapper.map(contestDto.getPlayerTwo(), PlayerResponseData.class)
        );
    }
}