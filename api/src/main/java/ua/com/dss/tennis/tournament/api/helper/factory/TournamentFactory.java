package ua.com.dss.tennis.tournament.api.helper.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.model.db.v1.ParticipantType;
import ua.com.dss.tennis.tournament.api.model.db.v1.Tournament;
import ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.TournamentDTO;

import java.util.List;
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
        contestFactory.removeTournamentContests(tournamentDto.getId());
    }

    public TournamentDTO populateTournamentDTO(Tournament tournament) {
        return converterHelper.convert(tournament, TournamentDTO.class);
    }

    public TournamentDTO populateTournamentDtoWithContests(Tournament tournament) {
        TournamentDTO tournamentDto = populateTournamentDTO(tournament);

        Iterable<ContestDTO> contests = getContestFactory(tournamentDto.getTournamentType(), tournamentDto
                .getParticipantType()).getContestDTOsWithParticipants(tournamentDto.getId());
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
}
