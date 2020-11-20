package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.service.MainMenuService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MainMenuCommand implements DssBotCommand {

    private final MainMenuService mainMenuService;

    public MainMenuCommand() {
        this.mainMenuService = new MainMenuService();
    }

    @Override
    public SendMessage execute(int userId, Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(), "Lets start with main menu.");
    }
}
