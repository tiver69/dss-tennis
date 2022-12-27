package ua.com.dss.tennis.tournament.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EliminationContestDTO extends ContestDTO implements Iterable<ContestDTO> {

    ContestDTO firstParentContestDto;
    ContestDTO secondParentContestDto;

    @Override
    public Object getParticipantOne() {
        if (firstParentContestDto.getParticipantOneId().equals(firstParentContestDto.getWinnerId()))
            return firstParentContestDto.getParticipantOne();
        if (firstParentContestDto.getParticipantTwoId().equals(firstParentContestDto.getWinnerId()))
            return firstParentContestDto.getParticipantTwo();
        return null;
    }

    @Override
    public Object getParticipantTwo() {
        if (secondParentContestDto.getParticipantOneId().equals(secondParentContestDto.getWinnerId()))
            return secondParentContestDto.getParticipantOne();
        if (secondParentContestDto.getParticipantTwoId().equals(secondParentContestDto.getWinnerId()))
            return secondParentContestDto.getParticipantTwo();
        return null;
    }

    @Override
    public Integer getParticipantOneId() {
        return firstParentContestDto.getWinnerId();
    }

    @Override
    public Integer getParticipantTwoId() {
        return secondParentContestDto.getWinnerId();
    }

    private void collectToList(List<ContestDTO> list) {
        list.add(firstParentContestDto);
        if (firstParentContestDto instanceof EliminationContestDTO) {
            ((EliminationContestDTO) firstParentContestDto).collectToList(list);
        }
        list.add(secondParentContestDto);
        if (secondParentContestDto instanceof EliminationContestDTO) {
            ((EliminationContestDTO) secondParentContestDto).collectToList(list);
        }
    }

    @Override
    public Iterator<ContestDTO> iterator() {
        List<ContestDTO> eliminationContestList = new ArrayList<>();
        eliminationContestList.add(this);
        collectToList(eliminationContestList);
        return eliminationContestList.iterator();
    }

    @Override
    public void forEach(Consumer<? super ContestDTO> action) {
        List<ContestDTO> eliminationContestList = new ArrayList<>();
        eliminationContestList.add(this);
        collectToList(eliminationContestList);
        eliminationContestList.forEach(action);
    }

    @Override
    public Spliterator<ContestDTO> spliterator() {
        List<ContestDTO> eliminationContestList = new ArrayList<>();
        eliminationContestList.add(this);
        collectToList(eliminationContestList);
        return eliminationContestList.spliterator();
    }
}
