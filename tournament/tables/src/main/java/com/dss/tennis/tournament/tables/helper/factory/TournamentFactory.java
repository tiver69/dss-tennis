package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.model.db.v1.ParticipantType;
import com.dss.tennis.tournament.tables.model.db.v1.Tournament;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentFactory {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private RoundContestFactory roundTournamentFactory;
    @Autowired
    private EliminationContestFactory eliminationTournamentFactory;
    @Autowired
    private SingleParticipantFactory singleParticipantFactory;
    @Autowired
    private DoubleParticipantFactory doubleParticipantFactory;

    public void createContestsForTournament(Tournament tournament, List<PlayerDTO> players,
                                            TournamentType tournamentType) {

        getContestFactory(tournamentType).createContests(tournament, players);
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
            List<PlayerDTO> players = getParticipantFactory(tournamentDto.getParticipantType())
                    .getTournamentPlayers(tournamentDto.getId());
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

    private AbstractParticipantFactory getParticipantFactory(ParticipantType type) {
        return type == ParticipantType.SINGLE ? singleParticipantFactory : doubleParticipantFactory;
    }
}
