package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.state.BotState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotCommandFactory {

    @Autowired
    private InitCommand initCommand;
    @Autowired
    private MainMenuCommand mainMenuCommand;
    @Autowired
    private MatchCommand matchCommand;
    @Autowired
    private CreateTournamentCommand createTournamentCommand;

    public DssBotCommand getCommand(BotState botState) {
        switch (botState) {
            case INIT:
                return initCommand;
            case MAIN_MENU:
                return mainMenuCommand;
            case ADD_MATCH:
                return matchCommand;
            case ASK_NAME:
            case ASK_TYPE:
            case ASK_PLAYERS:
            case ASK_ADDITIONAL_PLAYERS:
            case CONFIRM_TOURNAMENT:
            case SAVE_TOURNAMENT:
            case ROLLBACK_TOURNAMENT:
                return createTournamentCommand;
            default:
                return matchCommand;
        }
    }
}
