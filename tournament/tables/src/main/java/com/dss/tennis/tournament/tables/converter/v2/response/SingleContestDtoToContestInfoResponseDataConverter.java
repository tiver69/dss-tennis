package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.db.v2.SetType;
import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoAttributes;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.TechDefeat;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.ScoreDTO.SetScoreDTO;
import com.dss.tennis.tournament.tables.model.dto.SingleContestDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Map;

import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.CONTEST_INFO;

@AllArgsConstructor
public class SingleContestDtoToContestInfoResponseDataConverter implements Converter<SingleContestDTO,
        ContestInfoResponseData> {

    private String extraTournamentId;

    @Override
    public ContestInfoResponseData convert(MappingContext<SingleContestDTO, ContestInfoResponseData> mappingContext) {
        SingleContestDTO contest = mappingContext.getSource();

        return ContestInfoResponseData.builder()
                .id(contest.getId())
                .type(CONTEST_INFO.value)
                .attributes(convertContestInfoAttributes(contest))
                .links(Links.builder()
                        .self(String.format(CONTEST_INFO.selfLinkFormat, extraTournamentId, contest.getId()))
                        .build())
                .build();
    }


    private ContestInfoAttributes convertContestInfoAttributes(SingleContestDTO contestDto) {
        ContestInfoAttributes.ContestInfoAttributesBuilder builder = ContestInfoAttributes.builder()
                .participantOne(convertPlayerNameString(contestDto.getPlayerOne()))
                .participantTwo(convertPlayerNameString(contestDto.getPlayerTwo()))
                .techDefeat(convertTechDefeat(contestDto));

        if (contestDto.getScoreDto() != null) {
            Map<SetType, SetScoreDTO> sets = contestDto.getScoreDto().getSets();
            String setOneScore = convertSetScoreString(sets.get(SetType.SET_ONE));
            String setTwoScore = convertSetScoreString(sets.get(SetType.SET_TWO));
            String setThreeScore = convertSetScoreString(sets.get(SetType.SET_THREE));

            builder
                    .mainScore(setOneScore + " " + setTwoScore + " " + setThreeScore)
                    .tieBreak(sets.get(SetType.TIE_BREAK).getParticipantOneScore() + ":" + sets.get(SetType.TIE_BREAK)
                            .getParticipantTwoScore());
        }

        return builder.build();
    }

    private String convertPlayerNameString(PlayerDTO player) {
        return String.format("%s %s", player.getFirstName(), player.getLastName());
    }

    private String convertSetScoreString(SetScoreDTO setScore) {
        return String.format("%d:%d", setScore.getParticipantOneScore(), setScore.getParticipantTwoScore());
    }

    private TechDefeat convertTechDefeat(ContestDTO contestDTO) {
        return new TechDefeat(contestDTO.isParticipantOneTechDefeat(), contestDTO.isParticipantTwoTechDefeat());
    }
}
