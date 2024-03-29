package ua.com.dss.tennis.tournament.api.converter.response;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType;
import ua.com.dss.tennis.tournament.api.model.definitions.Links;
import ua.com.dss.tennis.tournament.api.model.definitions.SimpleResourceObject;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentResponse.TournamentRelationships;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentResponse.TournamentResponseAttributes;
import ua.com.dss.tennis.tournament.api.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.EliminationContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

import java.util.ArrayList;
import java.util.List;

import static ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType.ELIMINATION;
import static ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType.ROUND;
import static ua.com.dss.tennis.tournament.api.model.definitions.ResourceObjectType.*;

@AllArgsConstructor
public class TournamentDtoToTournamentResponseDataConverter implements Converter<TournamentDTO,
        TournamentResponseData> {

    private final ModelMapper modelMapper;

    @Override
    public TournamentResponseData convert(MappingContext<TournamentDTO, TournamentResponseData> context) {
        TournamentDTO tournamentDto = context.getSource();

        return TournamentResponseData.builder()
                .id(tournamentDto.getId())
                .attributes(convertTournamentAttributes(tournamentDto))
                .relationships(convertTournamentRelationships(tournamentDto))
                .links(Links.builder()
                        .self(String.format(TOURNAMENT.selfLinkFormat, tournamentDto.getId()))
                        .build())
                .build();
    }

    private TournamentResponseAttributes convertTournamentAttributes(TournamentDTO tournament) {
        return TournamentResponseAttributes.builder()
                .name(tournament.getName())
                .tournamentType(tournament.getTournamentType())
                .participantType(tournament.getParticipantType())
                .beginningDate(tournament.getBeginningDate())
                .participantsNumber(countParticipants(tournament.getTournamentType(), tournament.getContests()))
                .progress(countProgressPercentage(tournament.getContests()))
                .build();
    }

    private byte countParticipants(TournamentType type, Iterable<ContestDTO> contests) {
        if (contests == null) return 0;
        int contestsTotal = IterableUtils.size(contests);
        if (ROUND.equals(type))
            return (contestsTotal == 0) ? (byte) 0 : (byte) ((1 + Math.sqrt(1 + 8 * contestsTotal)) / 2);
        else if (ELIMINATION.equals(type)) {
            return (byte) (contestsTotal + 1);
        }
        return 0;
    }

    private byte countProgressPercentage(Iterable<ContestDTO> contests) {
        if (contests == null) return 0;
        byte contestsPlayed = 0;
        byte contestsTotal = 0;
        for (ContestDTO contestDto : contests) {
            if (contestDto.getWinnerId() != null || contestDto.getScoreDto().getTechDefeat().isTechDefeat())
                contestsPlayed++;
            contestsTotal++;
        }
        return (contestsTotal == 0) ? (byte) 0 : (byte) ((contestsPlayed * 100) / contestsTotal);
    }

    private TournamentRelationships convertTournamentRelationships(TournamentDTO tournamentDTO) {
        if (tournamentDTO.getContests() == null) return null;
        if (tournamentDTO.getContests() instanceof EliminationContestDTO) {
            return TournamentRelationships.builder()
                    .finalContest(new SimpleResourceObject(((EliminationContestDTO) tournamentDTO.getContests())
                            .getId(), ELIMINATION_CONTEST_INFO.value)).build();
        }
        List<SimpleResourceObject> contests = new ArrayList<>();
        tournamentDTO.getContests()
                .forEach(contest -> contests.add(new SimpleResourceObject(contest.getId(), CONTEST_INFO.value)));
        return contests.isEmpty() ? null : TournamentRelationships.builder()
                .contests(contests)
                .build();
    }
}
