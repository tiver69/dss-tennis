package ua.com.dss.tennis.tournament.api.helper.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.converter.ConverterHelper;
import ua.com.dss.tennis.tournament.api.exception.DetailedException;
import ua.com.dss.tennis.tournament.api.helper.ContestHelper;
import ua.com.dss.tennis.tournament.api.model.db.v1.TournamentType;
import ua.com.dss.tennis.tournament.api.model.db.v2.Contest;
import ua.com.dss.tennis.tournament.api.model.db.v2.EliminationContest;
import ua.com.dss.tennis.tournament.api.model.dto.ContestDTO;
import ua.com.dss.tennis.tournament.api.model.dto.EliminationContestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.*;

@Service
public abstract class EliminationContestFactory implements AbstractContestFactory {

    @Autowired
    protected ContestHelper contestHelper;
    @Autowired
    protected ConverterHelper converterHelper;

    public abstract Integer createFirstLineEliminationContest(Integer firstParticipantId, Integer secondParticipantId,
                                                              Integer tournamentId);

    protected abstract ContestDTO convertEliminationContestToBase(EliminationContestDTO eliminationContest);

    @Override
    public void createContestsForTournament(Integer tournamentId, List<Integer> newPlayerIds) {
        Integer currentFinalContestId = getFinalEliminationContestId(tournamentId);
        List<Integer> firstLineEliminationContestIds = new ArrayList<>();
        for (int i = 0; i < newPlayerIds.size(); i += 2)
            firstLineEliminationContestIds
                    .add(createFirstLineEliminationContest(newPlayerIds.get(i), newPlayerIds
                            .get(i + 1), tournamentId));

        Integer newFinalContestId = createContestsForTournamentRecursively(firstLineEliminationContestIds,
                tournamentId);

        if (currentFinalContestId != null && newFinalContestId != null)
            contestHelper.createNewEliminationContest(currentFinalContestId, newFinalContestId, tournamentId);
    }

    @Override
    public void removeParticipantFromTournament(Integer teamId, int tournamentId, boolean techDefeat) {
        throw new DetailedException(TOURNAMENT_TYPE_NOT_SUPPORTED, TournamentType.ELIMINATION);
    }

    @Override
    public void removeTournamentContests(int tournamentId) {
        EliminationContestDTO contests = getContestDTOs(tournamentId);
        if (contests == null) return;
        removeEliminationContestDtoRecursive(contests);
    }

    @Override
    public EliminationContestDTO getContestDTOs(Integer tournamentId) {
        List<Contest> contests = contestHelper.getTournamentContests(tournamentId);
        if (contests == null || contests.isEmpty()) return null;
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
                .orElseThrow(() -> new DetailedException(CONTEST_NOT_FOUND, contestId));
        if (getContestParticipantClass().isInstance(basicContest))
            return converterHelper.convert(basicContest, getContestParticipantDtoClass());

        return populateEliminationContestDtoRecursive((EliminationContest) basicContest, contests);
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
                                                             Integer tournamentId) {
        if (firstLineEliminationContestIds == null || firstLineEliminationContestIds.isEmpty()) return null;
        if (firstLineEliminationContestIds.size() == 1) return firstLineEliminationContestIds.get(0);

        List<Integer> nextLineEliminationContestIds = new ArrayList<>();
        for (int i = 0; i < firstLineEliminationContestIds.size(); i += 2) {
            nextLineEliminationContestIds
                    .add(contestHelper.createNewEliminationContest(firstLineEliminationContestIds
                            .get(i), firstLineEliminationContestIds.get(i + 1), tournamentId).getId());
        }
        return createContestsForTournamentRecursively(nextLineEliminationContestIds, tournamentId);
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
