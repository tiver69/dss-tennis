package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.participant.ParticipantHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TournamentFactory {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private RoundContestFactory roundTournamentFactory;
    @Autowired
    private EliminationContestFactory eliminationTournamentFactory;
    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private TeamHelper teamHelper;

    public boolean createContestForNewParticipants(TournamentDTO tournamentDto, Set<Integer> newParticipantIds) {
        AbstractContestFactory contestFactory = getContestFactory(tournamentDto.getTournamentType());
        if (tournamentDto.getParticipantType() == ParticipantType.SINGLE) {
            return contestFactory.createContestsForSingleTournament(tournamentDto.getId(), newParticipantIds);
        } else {
            return contestFactory.createContestsForDoubleTournament(tournamentDto.getId(), newParticipantIds);
        }
    }

    public TournamentDTO populateTournamentDTO(Tournament tournament) {
        return converterHelper.convert(tournament, TournamentDTO.class);
    }

    public TournamentDTO populateTournamentDTO(Tournament tournament, RequestParameter requestParameters) {
        TournamentDTO tournamentDto = populateTournamentDTO(tournament);

        if (requestParameters.isIncludeContests()) {
            Class<? extends ContestDTO> participantClass = getContestParticipantType(tournamentDto
                    .getParticipantType());
            List<ContestDTO> contests = getContestFactory(tournamentDto.getTournamentType())
                    .getContestDTOs(tournamentDto.getId(), participantClass);
            tournamentDto.setContests(contests);
        }
        if (requestParameters.isIncludePlayers()) {
            List<PlayerDTO> players = getParticipantHelper(tournamentDto.getParticipantType())
                    .getTournamentPlayerDTOs(tournamentDto.getId());
            tournamentDto.setPlayers(players);
        }
        return tournamentDto;
    }

    private Class<? extends ContestDTO> getContestParticipantType(ParticipantType participantType) {
        return participantType == ParticipantType.SINGLE ?
                SingleContestDTO.class :
                DoubleContestDTO.class;
    }

    private AbstractContestFactory getContestFactory(TournamentType type) {
        return type == TournamentType.ELIMINATION ? eliminationTournamentFactory : roundTournamentFactory;
    }

    private ParticipantHelper<?> getParticipantHelper(ParticipantType type) {
        return type == ParticipantType.SINGLE ? playerHelper : teamHelper;
    }
}
