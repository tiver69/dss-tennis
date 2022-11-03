package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentRelationships;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseAttributes;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.List;

import static com.dss.tennis.tournament.tables.model.db.v1.TournamentType.ELIMINATION;
import static com.dss.tennis.tournament.tables.model.db.v1.TournamentType.ROUND;
import static com.dss.tennis.tournament.tables.model.response.v1.ResourceObject.ResourceObjectType.*;

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
                .status(tournament.getStatus())
                .beginningDate(tournament.getBeginningDate())
                .participantsNumber(countParticipants(tournament.getTournamentType(), tournament.getContests()))
                .progress(countProgressPercentage(tournament.getContests()))
                .build();
    }

    private byte countParticipants(TournamentType type, Iterable<ContestDTO> contests) {
        if (contests == null) return 0;
        byte contestsTotal = 0;
        for (ContestDTO contestDto : contests) { //todo extend iterable contest to return size
            contestsTotal++;
        }
        if (ROUND.equals(type))
            return (contestsTotal == 0) ? (byte) 0 : (byte) ((1 + Math.sqrt(1 + 8 * contestsTotal)) / 2);
        else if (ELIMINATION.equals(type)) {
            return (byte) (contestsTotal + 1);
        }
        return 0;
    }

    //todo check progress with techDefeats
    private byte countProgressPercentage(Iterable<ContestDTO> contests) {
        if (contests == null) return 0;
        byte contestsPlayed = 0;
        byte contestsTotal = 0;
        for (ContestDTO contestDto : contests) { //todo extend iterable contest to return size
            if (contestDto.getWinnerId() != null) contestsPlayed++;
            contestsTotal++;
        }
        return (contestsTotal == 0) ? (byte) 0 : (byte) (contestsPlayed / contestsTotal);
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
