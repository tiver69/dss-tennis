package com.dss.tennis.tournament.bot.state.cache;

import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class DssTennisUserStateCache implements UserStateCache {

    public static final BotState DEFAULT_STATE = BotState.MAIN_MENU;

    private final Map<Integer, BotState> usersBotStates = new HashMap<>();
    private final Map<Integer, CreateTournamentDTO> usersCreateTournaments = new HashMap<>();

    @Override
    public CreateTournamentDTO getUserCreateTournamentDTO(int userId) {
        if (usersCreateTournaments.get(userId) == null) {
            saveUserCreateTournamentDTO(userId, CreateTournamentDTO.builder().players(new ArrayList<>()).build());
        }
        return usersCreateTournaments.get(userId);
    }

    @Override
    public void saveUserCreateTournamentDTO(int userId, CreateTournamentDTO createTournamentDTO) {
        usersCreateTournaments.put(userId, createTournamentDTO);
    }

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        return botState == null ? DEFAULT_STATE : botState;
    }

    @Override
    public void clearForUser(int userId) {
        saveUserCreateTournamentDTO(userId, new CreateTournamentDTO());
    }
}
