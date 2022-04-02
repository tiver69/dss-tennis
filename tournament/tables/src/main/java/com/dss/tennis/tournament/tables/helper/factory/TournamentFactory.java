package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.participant.ParticipantHelper;
import com.dss.tennis.tournament.tables.helper.participant.PlayerHelper;
import com.dss.tennis.tournament.tables.helper.participant.TeamHelper;
import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import com.dss.tennis.tournament.tables.model.dto.RequestParameter;
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TournamentFactory {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private RoundSingleContestFactory roundSingleContestFactory;
    @Autowired
    private RoundDoubleContestFactory roundDoubleContestFactory;
    @Autowired
    private EliminationContestFactory eliminationTournamentFactory;
    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private TeamHelper teamHelper;

    public boolean createContestForNewParticipants(TournamentDTO tournamentDto, Set<Integer> newParticipantIds) {
        AbstractContestFactory contestFactory = getContestFactory(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType());
        return contestFactory.createContestsForTournament(tournamentDto.getId(), newParticipantIds);
    }

    public void removeParticipantFromTournament(Integer participantId, TournamentDTO tournamentDto,
                                                boolean techDefeat) {
        AbstractContestFactory contestFactory = getContestFactory(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType());
        contestFactory.removeParticipantFromTournament(participantId, tournamentDto.getId(), techDefeat);
    }

    public TournamentDTO populateTournamentDTO(Tournament tournament) {
        return converterHelper.convert(tournament, TournamentDTO.class);
    }

    public TournamentDTO populateTournamentDTO(Tournament tournament, RequestParameter requestParameters) {
        TournamentDTO tournamentDto = populateTournamentDTO(tournament);

        if (requestParameters.isIncludeContests()) {
            List<ContestDTO> contests = getContestFactory(tournamentDto.getTournamentType(), tournamentDto
                    .getParticipantType()).getContestDTOs(tournamentDto.getId());
            tournamentDto.setContests(contests);
        }
        if (requestParameters.isIncludePlayers()) {
            List<PlayerDTO> players = getParticipantHelper(tournamentDto.getParticipantType())
                    .getTournamentPlayerDTOs(tournamentDto.getId());
            tournamentDto.setPlayers(players);
        }
        return tournamentDto;
    }

    private AbstractContestFactory getContestFactory(TournamentType type, ParticipantType participantType) {
        if (type == TournamentType.ROUND) {
            return participantType == ParticipantType.SINGLE ? roundSingleContestFactory : roundDoubleContestFactory;
        }
        return eliminationTournamentFactory;
    }

    private ParticipantHelper<?> getParticipantHelper(ParticipantType type) {
        return type == ParticipantType.SINGLE ? playerHelper : teamHelper;
    }
}
