package com.dss.tennis.tournament.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface DssBotCommand {
    SendMessage execute(int userId, Message message);
}
