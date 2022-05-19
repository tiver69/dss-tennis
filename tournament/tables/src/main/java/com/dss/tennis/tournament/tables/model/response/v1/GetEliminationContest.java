package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetEliminationContest<T extends GetParticipant> extends GetContest<T> implements Iterable<GetContest<T>> {

    GetContest<T> firstParentContest;
    GetContest<T> secondParentContest;

    @Override
    public Iterator<GetContest<T>> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super GetContest<T>> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<GetContest<T>> spliterator() {
        return Iterable.super.spliterator();
    }
}
