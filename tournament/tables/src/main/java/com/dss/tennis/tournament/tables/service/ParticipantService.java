package com.dss.tennis.tournament.tables.service;

import com.dss.tennis.tournament.tables.helper.PlayerHelper;
import com.dss.tennis.tournament.tables.model.dto.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

    @Autowired
    private PlayerHelper playerHelper;

    public PlayerDTO getPlayer(Integer playerId) {
        return playerHelper.getPlayer(playerId);
    }
}
