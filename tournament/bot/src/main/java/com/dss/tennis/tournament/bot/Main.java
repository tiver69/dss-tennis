package com.dss.tennis.tournament.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotApi = new TelegramBotsApi();
        try {
            telegramBotApi.registerBot(new DssTennisBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
