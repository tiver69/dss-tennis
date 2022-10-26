package com.dss.tennis.tournament.tables.converter.v2.response;

import com.dss.tennis.tournament.tables.model.definitions.Links;
import com.dss.tennis.tournament.tables.model.definitions.SimpleResourceObject;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.ContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.contest.ContestInfoResponse.EliminationContestInfoResponseData;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentRelationships;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseAttributes;
import com.dss.tennis.tournament.tables.model.definitions.tournament.TournamentResponse.TournamentResponseData;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.List;

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
                .included(convertTournamentIncluded(tournamentDto))
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
                .participantsNumber((byte) 100) //todo count participants
                .progress((byte) 100) //todo count player contests
                .build();
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
        return TournamentRelationships.builder()
                .contests(contests)
                .build();
    }

    private List<Object> convertTournamentIncluded(TournamentDTO tournamentDTO) {
        if (tournamentDTO.getContests() == null) return null;
        List<Object> included = new ArrayList<>();
        tournamentDTO.getContests().forEach(contest ->
                included.add(
                        modelMapper
                                .map(contest, contest instanceof EliminationContestDTO ?
                                        EliminationContestInfoResponseData.class : ContestInfoResponseData.class)));

        return included;
    }
}
