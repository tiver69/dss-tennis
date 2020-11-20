package com.dss.tennis.tournament.bot.util;

public class BotUtil {

    private static String COMMAND_PATTERN = "^/.+";

    public static boolean isCommand(String commandMessage) {
        return commandMessage.matches(COMMAND_PATTERN);
    }
}
