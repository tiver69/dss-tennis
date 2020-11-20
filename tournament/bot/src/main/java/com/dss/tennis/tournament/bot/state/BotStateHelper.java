package com.dss.tennis.tournament.bot.state;

import com.dss.tennis.tournament.bot.command.BotCommandFactory;
import com.dss.tennis.tournament.bot.command.DssBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class BotStateHelper {

    public SendMessage processInputMessage(BotState currentState, Message message) {
        DssBotCommand botCommand = BotCommandFactory.getCommand(currentState);
        return botCommand.execute(message.getFrom().getId(), message);
    }

    public SendMessage processInputMessage(BotState currentState, int userId, Message message) {
        DssBotCommand botCommand = BotCommandFactory.getCommand(currentState);
        return botCommand.execute(userId, message);
    }
}
