package com.dss.tennis.tournament.tables.repository;

import com.dss.tennis.tournament.tables.model.v1.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

    Optional<Player> findByFirstNameAndLastName(String firstName, String lastName);

}
