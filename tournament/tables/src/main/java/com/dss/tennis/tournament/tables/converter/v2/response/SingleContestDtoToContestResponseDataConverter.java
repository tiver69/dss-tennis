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
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Map;

import static com.dss.tennis.tournament.tables.model.db.v2.SetType.*;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.CONTEST;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.PLAYER;

@Getter
@Setter
@AllArgsConstructor
public class SingleContestDtoToContestResponseDataConverter implements Converter<SingleContestDTO,
        ContestResponseData> {

    private String extraTournamentId;

    @Override
    public ContestResponseData convert(MappingContext<SingleContestDTO, ContestResponseData> context) {
        SingleContestDTO contestDTO = context.getSource();

        return ContestResponseData.builder()
                .id(contestDTO.getId())
                .attributes(convertContestAttributes(contestDTO))
                .relationships(convertContestRelationships(contestDTO))
                .links(Links.builder()
                        .self(String.format(CONTEST.selfLinkFormat, extraTournamentId, contestDTO.getId()))
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

    private ContestRelationships convertContestRelationships(SingleContestDTO contestDto) {
        SimpleResourceObject winner = contestDto.getWinnerId() == null ? null : new SimpleResourceObject(contestDto
                .getWinnerId(), PLAYER.value);
        return ContestRelationships.builder()
                .participantOne(contestDto.participantOneId() == null ? null : new SimpleResourceObject(contestDto
                        .participantOneId(), PLAYER.value))
                .participantTwo(contestDto.participantTwoId() == null ? null : new SimpleResourceObject(contestDto
                        .participantTwoId(), PLAYER.value))
                .winner(winner)
                .build();
    }
}