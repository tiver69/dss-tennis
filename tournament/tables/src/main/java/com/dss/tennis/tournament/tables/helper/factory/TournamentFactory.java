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
import com.dss.tennis.tournament.tables.model.dto.TournamentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class TournamentFactory {

    @Autowired
    private ConverterHelper converterHelper;
    @Autowired
    private RoundSingleContestFactory roundSingleContestFactory;
    @Autowired
    private RoundDoubleContestFactory roundDoubleContestFactory;
    @Autowired
    private EliminationSingleContestFactory eliminationSingleContestFactory;
    @Autowired
    private EliminationDoubleContestFactory eliminationDoubleContestFactory;
    @Autowired
    private PlayerHelper playerHelper;
    @Autowired
    private TeamHelper teamHelper;

    public void createContestForNewParticipants(TournamentDTO tournamentDto, List<Integer> newParticipantIds) {
        AbstractContestFactory contestFactory = getContestFactory(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType());
        contestFactory.createContestsForTournament(tournamentDto.getId(), newParticipantIds);
    }

    public void removeParticipantFromTournament(Integer participantId, TournamentDTO tournamentDto,
                                                boolean techDefeat) {
        AbstractContestFactory contestFactory = getContestFactory(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType());
        contestFactory.removeParticipantFromTournament(participantId, tournamentDto.getId(), techDefeat);
    }

    public void removeTournamentContests(TournamentDTO tournamentDto) {
        AbstractContestFactory contestFactory = getContestFactory(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType());
        contestFactory.
                removeTournamentContests(tournamentDto.getId());
    }

    public TournamentDTO populateTournamentDTO(Tournament tournament) {
        return converterHelper.convert(tournament, TournamentDTO.class);
    }

    public TournamentDTO populateTournamentDtoExtended(Tournament tournament) {
        TournamentDTO tournamentDto = populateTournamentDTO(tournament);

        //todo refactor
        Map<Integer, PlayerDTO> players = getParticipantHelper(tournamentDto.getParticipantType())
                .getTournamentPlayerDtoMap(tournamentDto.getId());
        Iterable<ContestDTO> contests = getContestFactory(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType()).getContestDTOs(tournamentDto.getId(), players);
        tournamentDto.setContests(contests);

        return tournamentDto;
    }

    public Consumer<Integer> getParticipantEnrollingQuantityValidationRule(TournamentType type,
                                                                           ParticipantType participantType) {
        return getContestFactory(type, participantType).getParticipantEnrollingQuantityValidationRule();
    }

    public ContestDTO getTournamentContestDTO(Integer contestId, TournamentDTO tournamentDTO) {
        AbstractContestFactory contestFactory = getContestFactory(tournamentDTO.getTournamentType(), tournamentDTO
                .getParticipantType());
        return contestFactory.getContestDTO(contestId, tournamentDTO.getId());
    }

    private AbstractContestFactory getContestFactory(TournamentType type, ParticipantType participantType) {
        if (type == TournamentType.ROUND) {
            return participantType == ParticipantType.SINGLE ? roundSingleContestFactory : roundDoubleContestFactory;
        }
        return participantType == ParticipantType.SINGLE ? eliminationSingleContestFactory :
                eliminationDoubleContestFactory;
    }

    private ParticipantHelper<?, ?> getParticipantHelper(ParticipantType type) {
        return type == ParticipantType.SINGLE ? playerHelper : teamHelper;
    }
}
