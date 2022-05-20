package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.FORBIDDEN_PARTICIPANT_QUANTITY;

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

    public List<ContestDTO> getContestDTOs(Integer tournamentId) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        return contests.stream()
                .map(contest -> converterHelper.convert(contest, getContestParticipantDtoClass()))
                .collect(Collectors.toList());
    }

    protected void removeContests(Supplier<List<Contest>> contestsToRemove) {
        contestsToRemove.get().stream().map(Contest::getId).forEach(contestHelper::removeContestById);
    }
}
