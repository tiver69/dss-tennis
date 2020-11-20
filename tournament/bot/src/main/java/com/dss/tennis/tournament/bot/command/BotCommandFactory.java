package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.state.BotState;

public class BotCommandFactory {
    public static DssBotCommand getCommand(BotState botState) {
        switch (botState) {
            case INIT:
                return new InitCommand();
            case MAIN_MENU:
                return new MainMenuCommand();
            case ADD_MATCH:
                return new MatchCommand();
            case ASK_NAME:
            case ASK_TYPE:
            case ASK_PLAYERS:
            case ASK_ADDITIONAL_PLAYERS:
            case CONFIRM_TOURNAMENT:
            case SAVE_TOURNAMENT:
            case ROLLBACK_TOURNAMENT:
                return new CreateTournamentCommand();
            default:
                return new MainMenuCommand();
        }
    }
}
