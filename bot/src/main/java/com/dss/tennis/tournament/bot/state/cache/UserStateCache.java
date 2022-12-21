package com.dss.tennis.tournament.bot.state.cache;

import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;

public interface UserStateCache {

    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    CreateTournamentDTO getUserCreateTournamentDTO(int userId);

    void saveUserCreateTournamentDTO(int userId, CreateTournamentDTO createTournamentDTO);

    void clearForUser(int userId);
}
