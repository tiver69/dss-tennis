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
import com.dss.tennis.tournament.tables.model.definitions.team.TeamResponse.TeamResponseData;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.DoubleContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
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
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.TEAM;

@Getter
@Setter
@AllArgsConstructor
public class DoubleContestDtoToContestResponseDataConverter implements Converter<DoubleContestDTO,
        ContestResponseData> {

    private ModelMapper modelMapper;

    @Override
    public ContestResponseData convert(MappingContext<DoubleContestDTO, ContestResponseData> context) {
        DoubleContestDTO contestDTO = context.getSource();

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
                .score(convertScore(contestDTO)) //todo tech defeat is not part of score
                .build();
    }

    private TechDefeat convertTechDefeat(ContestDTO contestDTO) {
        return new TechDefeat(contestDTO.isParticipantOneTechDefeat(), contestDTO.isParticipantTwoTechDefeat());
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

    private ContestRelationships convertContestRelationships(DoubleContestDTO contestDto) {
        SimpleResourceObject winner = contestDto.getWinnerId() == null ? null : new SimpleResourceObject(contestDto
                .getWinnerId(), TEAM.value);
        return ContestRelationships.builder()
                .participantOne(new SimpleResourceObject(contestDto.participantOneId(), TEAM.value))
                .participantTwo(new SimpleResourceObject(contestDto.participantTwoId(), TEAM.value))
                .winner(winner)
                .build();
    }

    private List<Object> convertContestIncluded(DoubleContestDTO contestDto) {
        return List.of(
                modelMapper.map(contestDto.getTeamOne(), TeamResponseData.class),
                modelMapper.map(contestDto.getTeamTwo(), TeamResponseData.class)
        );
    }
}