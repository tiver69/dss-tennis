package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.helper.KeyboardMarkupHelper;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class InitCommand implements DssBotCommand {

    public static final String INIT_TEXT = "What are we going to do today?";

    @Autowired
    private KeyboardMarkupHelper keyboardMarkupHelper;
    @Autowired
    private DssTennisUserStateCache userStateCache;

    @Override
    public SendMessage execute(int userId, Message message) {
        userStateCache.clearForUser(userId);
        return processTournamentOperations(message);
    }

    private SendMessage processTournamentOperations(Message message) {
        SendMessage replyWithButtonsMessage = new SendMessage(message.getChatId(), INIT_TEXT);
        replyWithButtonsMessage.setReplyMarkup(keyboardMarkupHelper.getTournamentActionMarkup());
        return replyWithButtonsMessage;
    }
}
