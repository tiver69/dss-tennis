package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.helper.MainMenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MainMenuCommand implements DssBotCommand {

    public static final String START_MESSAGE = "Lets start with main menu.";

    @Autowired
    private MainMenuHelper mainMenuHelper;

    @Override
    public SendMessage execute(int userId, Message message) {
        return mainMenuHelper.getMainMenuMessage(message.getChatId(), START_MESSAGE);
    }
}
