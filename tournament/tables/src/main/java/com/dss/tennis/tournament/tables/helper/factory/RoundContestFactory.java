package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class RoundContestFactory implements AbstractContestFactory {

    @Autowired
    protected ContestHelper contestHelper;
    @Autowired
    protected ContestRepository contestRepository;
    @Autowired
    protected ConverterHelper converterHelper;

    @Override
    public List<ContestDTO> getContestDTOs(Integer tournamentId) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        return contests.stream()
                .map(contest -> converterHelper.convert(contest, getContestParticipantClass()))
                .collect(Collectors.toList());
    }

    protected void removeContests(Supplier<List<Contest>> contestsToRemove) {
        contestsToRemove.get().stream().map(Contest::getId).forEach(contestHelper::removeContestById);
    }

    @Override
    public void removeTournamentContests(int tournamentId) {
        removeContests(() -> contestHelper.getTournamentContests(tournamentId));
    }

    protected abstract Class<? extends ContestDTO> getContestParticipantClass();
}
