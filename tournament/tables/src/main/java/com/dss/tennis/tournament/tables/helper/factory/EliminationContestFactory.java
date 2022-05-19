package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.EliminationContest;
import com.dss.tennis.tournament.tables.model.db.v2.SingleContest;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.TOURNAMENT_TYPE_NOT_SUPPORTED;

@Service
public abstract class EliminationContestFactory implements AbstractContestFactory {

    @Autowired
    protected ContestHelper contestHelper;
    @Autowired
    protected ConverterHelper converterHelper;

    @Override
    public boolean createContestsForTournament(Integer tournamentId, Set<Integer> newPlayerIds) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }

    @Override
    public void removeParticipantFromTournament(Integer teamId, int tournamentId, boolean techDefeat) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }

    @Override
    public void removeTournamentContests(int tournamentId) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }

    public EliminationContestDTO getContestDTOs(Integer tournamentId) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        if (contests.isEmpty()) return null;
        Set<Integer> preFinalContestIds = contests.stream().filter(EliminationContest.class::isInstance)
                .flatMap(contest -> {
                    EliminationContest eliminationContest = (EliminationContest) contest;
                    return Stream.of(eliminationContest.getFirstParentContestId(), eliminationContest
                            .getSecondParentContestId());
                }).collect(Collectors.toSet());

        EliminationContest finalContest = (EliminationContest) contests.stream()
                .filter(contest -> !preFinalContestIds.contains(contest.getId())).findAny()
                .get();

        return populateEliminationContestDtoRecursive(finalContest, contests);
    }

    private EliminationContestDTO populateEliminationContestDtoRecursive(EliminationContest eliminationContest,
                                                                         List<Contest> contests) {
        EliminationContestDTO eliminationContestDTO = converterHelper
                .convert(eliminationContest, EliminationContestDTO.class);

        Contest firstParent = contests.stream()
                .filter(c -> c.getId() == eliminationContest.getFirstParentContestId())
                .findFirst().get();
        Contest secondParent = contests.stream()
                .filter(c -> c.getId() == eliminationContest.getSecondParentContestId())
                .findFirst().get();

        if (firstParent instanceof EliminationContest) {
            eliminationContestDTO.setFirstParentContestDto(
                    populateEliminationContestDtoRecursive((EliminationContest) firstParent, contests));
        }
        if (secondParent instanceof EliminationContest) {
            eliminationContestDTO.setSecondParentContestDto(
                    populateEliminationContestDtoRecursive((EliminationContest) secondParent, contests));
        }
        if (firstParent instanceof SingleContest && secondParent instanceof SingleContest) {
            eliminationContestDTO
                    .setFirstParentContestDto(converterHelper.convert(firstParent, getContestParticipantClass()));
            eliminationContestDTO
                    .setSecondParentContestDto(converterHelper.convert(secondParent, getContestParticipantClass()));
        }
        return eliminationContestDTO;
    }
}
