package ua.com.dss.tennis.tournament.api.helper.factory;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.helper.ContestHelper;
import ua.com.dss.tennis.tournament.api.model.db.v2.Contest;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.repository.ContestRepository;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.CONTEST_NOT_FOUND;
import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.FORBIDDEN_PARTICIPANT_QUANTITY;

public abstract class RoundContestFactory implements AbstractContestFactory {

    @Autowired
    protected ContestHelper contestHelper;
    @Autowired
    protected ContestRepository contestRepository;
    @Autowired
    protected ConverterHelper converterHelper;

    @Override
    public void removeTournamentContests(int tournamentId) {
        removeContests(() -> contestHelper.getTournamentContests(tournamentId));
    }

    @Override
    public Consumer<Integer> getParticipantEnrollingQuantityValidationRule() {
        return (Integer commonQuantity) -> {
            if (commonQuantity <= 1 || commonQuantity > 64) throw new DetailedException(FORBIDDEN_PARTICIPANT_QUANTITY);
        };
    }

    @Override
    public List<ContestDTO> getContestDTOs(Integer tournamentId) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        return contests.stream()
                .map(contest -> converterHelper.convert(contest, getContestParticipantDtoClass()))
                .collect(Collectors.toList());
    }

    @Override
    public ContestDTO getBasicContestDTO(Integer contestId, Integer tournamentId) {
        Contest contest = contestRepository.findByIdAndTournamentId(contestId, tournamentId)
                .orElseThrow(() -> new DetailedException(CONTEST_NOT_FOUND, contestId));
        return converterHelper.convert(Hibernate.unproxy(contest), getContestParticipantDtoClass());
    }

    protected void removeContests(Supplier<List<Contest>> contestsToRemove) {
        contestsToRemove.get().forEach(contestHelper::removeContest);
    }
}
