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
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Map;

import static com.dss.tennis.tournament.tables.model.db.v2.SetType.*;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.*;

@Getter
@Setter
@AllArgsConstructor
public class EliminationContestDtoToContestResponseDataConverter implements Converter<EliminationContestDTO,
        ContestResponseData> {

    private String extraTournamentId;

    @Override
    public ContestResponseData convert(MappingContext<EliminationContestDTO, ContestResponseData> context) {
        EliminationContestDTO contestDTO = context.getSource();

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

    private ContestRelationships convertContestRelationships(EliminationContestDTO contestDto) {
        String participantResourceType = getParticipantType(contestDto);
        if (participantResourceType == null) return null;

        SimpleResourceObject participantOne = contestDto
                .getParticipantOne() != null ? new SimpleResourceObject(contestDto
                .participantOneId(), participantResourceType) : null;
        SimpleResourceObject participantTwo = contestDto
                .getParticipantTwo() != null ? new SimpleResourceObject(contestDto
                .participantTwoId(), participantResourceType) : null;
        SimpleResourceObject winner = contestDto.getWinnerId() == null ? null : new SimpleResourceObject(contestDto
                .getWinnerId(), participantResourceType);

        return ContestRelationships.builder()
                .participantOne(participantOne)
                .participantTwo(participantTwo)
                .winner(winner)
                .build();
    }

    private String getParticipantType(EliminationContestDTO contestDto) {
        if (contestDto.getParticipantOne() != null)
            return (contestDto.getParticipantOne() instanceof PlayerDTO) ? PLAYER.value : TEAM.value;
        if (contestDto.getParticipantTwo() != null)
            return (contestDto.getParticipantTwo() instanceof PlayerDTO) ? PLAYER.value : TEAM.value;
        return null;
    }
}
