package com.dss.tennis.tournament.bot.state;

import com.dss.tennis.tournament.bot.command.BotCommandFactory;
import com.dss.tennis.tournament.bot.command.DssBotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class BotStateHelper {

    @Autowired
    private BotCommandFactory commandFactory;

    public SendMessage processInputMessage(BotState currentState, Message message) {
        DssBotCommand botCommand = commandFactory.getCommand(currentState);
        return botCommand.execute(message.getFrom().getId(), message);
    }

    public SendMessage processInputMessage(BotState currentState, int userId, Message message) {
        DssBotCommand botCommand = commandFactory.getCommand(currentState);
        return botCommand.execute(userId, message);
    }
}
