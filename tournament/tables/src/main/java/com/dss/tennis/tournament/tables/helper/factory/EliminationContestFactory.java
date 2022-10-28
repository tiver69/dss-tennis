package com.dss.tennis.tournament.tables.helper.factory;

import com.dss.tennis.tournament.tables.converter.ConverterHelper;
import com.dss.tennis.tournament.tables.exception.DetailedException;
import com.dss.tennis.tournament.tables.helper.ContestHelper;
import com.dss.tennis.tournament.tables.model.db.v1.TournamentType;
import com.dss.tennis.tournament.tables.model.db.v2.Contest;
import com.dss.tennis.tournament.tables.model.db.v2.EliminationContest;
import com.dss.tennis.tournament.tables.model.dto.ContestDTO;
import com.dss.tennis.tournament.tables.model.dto.EliminationContestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.*;

@Service
public abstract class EliminationContestFactory implements AbstractContestFactory {

    @Autowired
    protected ContestHelper contestHelper;
    @Autowired
    protected ConverterHelper converterHelper;

    public abstract Integer createFirstLineEliminationContest(Integer firstParticipantId, Integer secondParticipantId,
                                                              Integer tournamentId, boolean shouldCreateScore);

    protected abstract ContestDTO convertEliminationContestToBase(EliminationContestDTO eliminationContest);

    @Override
    public void createContestsForTournament(Integer tournamentId, List<Integer> newPlayerIds, boolean shouldCreateScore) {
        Integer currentFinalContestId = getFinalEliminationContestId(tournamentId);
        List<Integer> firstLineEliminationContestIds = new ArrayList<>();
        for (int i = 0; i < newPlayerIds.size(); i += 2)
            firstLineEliminationContestIds
                    .add(createFirstLineEliminationContest(newPlayerIds.get(i), newPlayerIds.get(i + 1), tournamentId, shouldCreateScore));

        Integer newFinalContestId = createContestsForTournamentRecursively(firstLineEliminationContestIds,
                tournamentId, shouldCreateScore);

        if (currentFinalContestId != null && newFinalContestId != null)
            contestHelper.createNewEliminationContest(currentFinalContestId, newFinalContestId, tournamentId, shouldCreateScore);
    }

    @Override
    public void removeParticipantFromTournament(Integer teamId, int tournamentId, boolean techDefeat) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }

    @Override
    public void removeTournamentContests(int tournamentId) {
        EliminationContestDTO contests = getContestDTOs(tournamentId);
        removeEliminationContestDtoRecursive(contests);
    }

    @Override
    public EliminationContestDTO getContestDTOs(Integer tournamentId) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        Set<Integer> preFinalContestIds = contests.stream().filter(EliminationContest.class::isInstance)
                .flatMap(contest -> {
                    EliminationContest eliminationContest = (EliminationContest) contest;
                    return Stream.of(eliminationContest.getFirstParentContestId(), eliminationContest
                            .getSecondParentContestId());
                }).collect(Collectors.toSet());

        EliminationContest finalContest = (EliminationContest) contests.stream()
                .filter(contest -> !preFinalContestIds.contains(contest.getId())).findAny().orElse(null);

        return populateEliminationContestDtoRecursive(finalContest, contests);
    }

    @Override
    public Consumer<Integer> getParticipantEnrollingQuantityValidationRule() {
        return (Integer commonQuantity) -> {
            if (commonQuantity < 4 || commonQuantity > 64)
                throw new DetailedException(FORBIDDEN_PARTICIPANT_QUANTITY, commonQuantity);
            Stream.of(4, 8, 16, 32, 64).filter(commonQuantity::equals).findAny()
                    .orElseThrow(() -> new DetailedException(FORBIDDEN_PARTICIPANT_QUANTITY, commonQuantity));
        };
    }

    @Override
    public ContestDTO getBasicContestDTO(Integer contestId, Integer tournamentId) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        Contest basicContest = contests.stream().filter(contest -> contest.getId() == contestId).findFirst()
                .orElseThrow(() -> new DetailedException(CONTEST_NOT_FOUND));
        if (getContestParticipantClass().isInstance(basicContest))
            return converterHelper.convert(basicContest, getContestParticipantDtoClass());

        EliminationContestDTO eliminationContest =
                populateEliminationContestDtoRecursive((EliminationContest) basicContest, contests);
        if (eliminationContest.getSecondParentContestDto().getWinnerId() == null || eliminationContest
                .getFirstParentContestDto().getWinnerId() == null)
            throw new DetailedException(CONTEST_NOT_REACHED);

        return convertEliminationContestToBase(eliminationContest);
    }

    protected Integer getFinalEliminationContestId(Integer tournamentId) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        Set<Integer> preFinalContestIds = contests.stream().filter(EliminationContest.class::isInstance)
                .flatMap(contest -> {
                    EliminationContest eliminationContest = (EliminationContest) contest;
                    return Stream.of(eliminationContest.getFirstParentContestId(), eliminationContest
                            .getSecondParentContestId());
                }).collect(Collectors.toSet());

        return contests.stream().map(Contest::getId)
                .filter(contestId -> !preFinalContestIds.contains(contestId)).findAny().orElse(null);
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
        if (getContestParticipantClass().isInstance(firstParent) && getContestParticipantClass()
                .isInstance(secondParent)) {
            eliminationContestDTO
                    .setFirstParentContestDto(converterHelper.convert(firstParent, getContestParticipantDtoClass()));
            eliminationContestDTO
                    .setSecondParentContestDto(converterHelper.convert(secondParent, getContestParticipantDtoClass()));
        }
        return eliminationContestDTO;
    }

    protected Integer createContestsForTournamentRecursively(List<Integer> firstLineEliminationContestIds,
                                                             Integer tournamentId, boolean shouldCreateScore) {
        if (firstLineEliminationContestIds == null || firstLineEliminationContestIds.isEmpty()) return null;
        if (firstLineEliminationContestIds.size() == 1) return firstLineEliminationContestIds.get(0);

        List<Integer> nextLineEliminationContestIds = new ArrayList<>();
        for (int i = 0; i < firstLineEliminationContestIds.size(); i += 2) {
            nextLineEliminationContestIds
                    .add(contestHelper.createNewEliminationContest(firstLineEliminationContestIds
                            .get(i), firstLineEliminationContestIds.get(i + 1), tournamentId, shouldCreateScore).getId());
        }
        return createContestsForTournamentRecursively(nextLineEliminationContestIds, tournamentId, shouldCreateScore);
    }

    private void removeEliminationContestDtoRecursive(ContestDTO eliminationContest) {
        contestHelper.removeContestById(eliminationContest.getId());
        if (eliminationContest instanceof EliminationContestDTO) {
            removeEliminationContestDtoRecursive(((EliminationContestDTO) eliminationContest)
                    .getFirstParentContestDto());
            removeEliminationContestDtoRecursive(((EliminationContestDTO) eliminationContest)
                    .getSecondParentContestDto());
        }
    }
}
