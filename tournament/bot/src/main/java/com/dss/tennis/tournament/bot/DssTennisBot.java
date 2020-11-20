package com.dss.tennis.tournament.bot;

import lombok.Getter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DssTennisBot extends TelegramLongPollingBot {

    @Getter
    private DssTennisBotFacade facade;

    public DssTennisBot() {
        this.facade = new DssTennisBotFacade();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(facade.handleUpdate(update));
        } catch (TelegramApiException e) {
            System.out.println("Message was not sent to chatId " + update.getUpdateId().toString());
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "dss_tennis_bot";
    }

    @Override
    public String getBotToken() {
        return "1469972096:AAGc8NMObvdVbZ1f-PHgfu3J1Tjwes09_NM";
    }
}
