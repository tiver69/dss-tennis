package com.dss.tennis.tournament.bot.state.cache;

import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;

import java.util.HashMap;
import java.util.Map;

public class DssTennisUserStateCache implements UserStateCache {

    private final Map<Integer, BotState> usersBotStates = new HashMap<>();
    private final Map<Integer, CreateTournamentDTO> usersCreateTournaments = new HashMap<>();

    private DssTennisUserStateCache() {
    }

    @Override
    public CreateTournamentDTO getUserCreateTournamentDTO(int userId) {
        if (usersCreateTournaments.get(userId) == null) {
            saveUserCreateTournamentDTO(userId, new CreateTournamentDTO());
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
        return botState == null ? BotState.MAIN_MENU : botState;
    }

    @Override
    public void clearForUser(int userId) {
        saveUserCreateTournamentDTO(userId, new CreateTournamentDTO());
    }

    private static class SingletonHelper {
        private static final DssTennisUserStateCache INSTANCE = new DssTennisUserStateCache();
    }

    public static DssTennisUserStateCache getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
