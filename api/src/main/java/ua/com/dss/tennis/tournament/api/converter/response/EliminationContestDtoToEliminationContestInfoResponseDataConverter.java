package ua.com.dss.tennis.tournament.api.converter.response;

import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.ContestInfoAttributes;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.EliminationContestInfoRelationships;
import ua.com.dss.tennis.tournament.api.model.definitions.contest.ContestInfoResponse.EliminationContestInfoResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.EliminationContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.PlayerDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TeamDTO;

import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.CONTEST_INFO;
import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.ELIMINATION_CONTEST_INFO;

@AllArgsConstructor
public class EliminationContestDtoToEliminationContestInfoResponseDataConverter extends ContestDtoToContestInfoResponseDataConverter
        implements Converter<EliminationContestDTO, EliminationContestInfoResponseData> {

    private final Integer extraTournamentId;

    @Override
    public EliminationContestInfoResponseData convert(MappingContext<EliminationContestDTO,
            EliminationContestInfoResponseData> context) {
        EliminationContestDTO eliminationContest = context.getSource();

        return EliminationContestInfoResponseData.builder()
                .id(eliminationContest.getId())
                .type(ELIMINATION_CONTEST_INFO.value)
                .attributes(convertContestInfoAttributes(eliminationContest))
                .relationships(convertEliminationContestInfoRelationships(eliminationContest))
                .links(Links.builder()
                        .self(String
                                .format(ELIMINATION_CONTEST_INFO.selfLinkFormat, extraTournamentId, eliminationContest
                                        .getId()))
                        .build())
                .build();
    }

    private ContestInfoAttributes convertContestInfoAttributes(EliminationContestDTO eliminationContestDto) {
        if (eliminationContestDto.getParticipantOneId() == null && eliminationContestDto.getParticipantTwoId() == null)
            return null;

        ContestInfoAttributes.ContestInfoAttributesBuilder builder = ContestInfoAttributes.builder()
                .participantOne(convertParticipantNameString(eliminationContestDto.getParticipantOne()))
                .participantTwo(convertParticipantNameString(eliminationContestDto.getParticipantTwo()));
        if (eliminationContestDto.getScoreDto() != null && eliminationContestDto
                .getParticipantOneId() != null && eliminationContestDto.getParticipantTwoId() != null)
            convertContestScoreInfoAttributes(eliminationContestDto.getScoreDto(), builder);

        return builder.build();
    }

    private String convertParticipantNameString(Object participant) {
        if (participant != null) {
            return participant instanceof PlayerDTO ? convertPlayerNameString((PlayerDTO) participant) :
                    convertTeamNameString((TeamDTO) participant);
        }
        return null;
    }

    private String convertPlayerNameString(PlayerDTO player) {
        return String.format("%s %s", player.getFirstName(), player.getLastName());
    }

    private String convertTeamNameString(TeamDTO team) {
        return String
                .format("%s %s - %s %s", team.getPlayerOne().getFirstName(), team.getPlayerOne().getLastName(), team
                        .getPlayerTwo().getFirstName(), team.getPlayerTwo().getLastName());
    }

    private EliminationContestInfoRelationships convertEliminationContestInfoRelationships(EliminationContestDTO eliminationContestDto) {
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