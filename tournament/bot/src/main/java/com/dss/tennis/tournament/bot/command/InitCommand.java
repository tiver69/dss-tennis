package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.service.KeyboardMarkupService;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class InitCommand implements DssBotCommand {

    private final KeyboardMarkupService keyboardMarkupService;
    private final DssTennisUserStateCache userStateCache;

    public InitCommand() {
        this.userStateCache = DssTennisUserStateCache.getInstance();
        this.keyboardMarkupService = new KeyboardMarkupService();
    }

    @Override
    public SendMessage execute(int userId, Message message) {
        userStateCache.clearForUser(userId);
        return processTournamentOperations(message);
    }

    private SendMessage processTournamentOperations(Message message) {
        SendMessage replyWithButtonsMessage = new SendMessage(message.getChatId(), "What are we going to do today?");
        replyWithButtonsMessage.setReplyMarkup(keyboardMarkupService.getInlineMessageButtons());
        return replyWithButtonsMessage;
    }
}
