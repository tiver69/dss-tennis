package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoAttributes;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.EliminationContestInfoRelationships;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.EliminationContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.TechDefeat;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Map;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.*;

public class EliminationContestDtoToEliminationContestInfoResponseDataConverter implements Converter<EliminationContestDTO, EliminationContestInfoResponseData> {

    @Override
    public EliminationContestInfoResponseData convert(MappingContext<EliminationContestDTO,
            EliminationContestInfoResponseData> context) {
        EliminationContestDTO eliminationContest = context.getSource();

        return EliminationContestInfoResponseData.builder()
                .id(eliminationContest.getId())
                .type(ELIMINATION_CONTEST_INFO.value)
                .attributes(convertContestInfoAttributes(eliminationContest))
                .relationships(convertEliminationContesInfoRelationships(eliminationContest))
                .links(Links.builder()
                        .self(String.format(CONTEST.selfLinkFormat, 1, eliminationContest
                                .getId())) //todo tournamentId not here
                        .build())
                .build();
    }

    private ContestInfoAttributes convertContestInfoAttributes(EliminationContestDTO eliminationContestDto) {
        ContestInfoAttributes.ContestInfoAttributesBuilder builder = ContestInfoAttributes.builder()
                .participantOne("TODO")
                .participantTwo("TODO");

        if (eliminationContestDto.getScoreDto() != null) {
            Map<SetType, SetScoreDTO> sets = eliminationContestDto.getScoreDto().getSets();
            String setOneScore = convertSetScoreString(sets.get(SetType.SET_ONE));
            String setTwoScore = convertSetScoreString(sets.get(SetType.SET_TWO));
            String setThreeScore = convertSetScoreString(sets.get(SetType.SET_THREE));

            builder
                    .mainScore(setOneScore + " " + setTwoScore + " " + setThreeScore)
                    .tieBreak(sets.get(SetType.TIE_BREAK).getParticipantOneScore() + ":" + sets.get(SetType.TIE_BREAK)
                            .getParticipantTwoScore())
                    .techDefeat(new TechDefeat());
        }

        return builder.build();
    }

    private String convertSetScoreString(SetScoreDTO setScore) {
        return String.format("%d:%d", setScore.getParticipantOneScore(), setScore.getParticipantTwoScore());
    }

    private EliminationContestInfoRelationships convertEliminationContesInfoRelationships(EliminationContestDTO eliminationContestDto) {
        return EliminationContestInfoRelationships.builder()
                .firstParentContest(convertSimpleResourceObject(eliminationContestDto
                        .getFirstParentContestDto()))
                .secondParentContest(convertSimpleResourceObject(eliminationContestDto
                        .getSecondParentContestDto()))
                .build();
    }

    private SimpleResourceObject convertSimpleResourceObject(ContestDTO contestDTO) {
        return new SimpleResourceObject(contestDTO
                .getId(),
                (contestDTO instanceof EliminationContestDTO ? ELIMINATION_CONTEST_INFO : CONTEST_INFO).value);
    }
}