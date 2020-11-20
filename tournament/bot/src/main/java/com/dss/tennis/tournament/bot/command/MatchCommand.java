package com.dss.tennis.tournament.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MatchCommand implements DssBotCommand {

    private static String IDENTIFIER = "match";
    private static String DESCRIPTION = "Add Match Results";

    @Override
    public SendMessage execute(int userId, Message message) {
        return null;
    }
}
