package com.dss.tennis.tournament.bot.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import static com.dss.tennis.tournament.bot.helper.MainMenuHelper.START_KEYBOARD;
import static org.junit.jupiter.api.Assertions.*;

public class MainMenuHelperTest {

    public static final long CHAT_ID = 1;
    public static final String TEXT_MESSAGE = "textMessage";

    private MainMenuHelper testInstance = new MainMenuHelper();

    @Test
    public void shouldCreateMainMenu() {
        SendMessage result = testInstance.getMainMenuMessage(CHAT_ID, TEXT_MESSAGE);

        Assertions.assertAll(
                () -> assertEquals(Long.toString(CHAT_ID), result.getChatId()),
                () -> assertEquals(TEXT_MESSAGE, result.getText()),
                () -> assertNotNull(result.getReplyMarkup())
        );
        ReplyKeyboardMarkup resultReplyKeyboardMarkup = (ReplyKeyboardMarkup) result.getReplyMarkup();
        Assertions.assertAll(
                () -> assertTrue(resultReplyKeyboardMarkup.getSelective()),
                () -> assertTrue(resultReplyKeyboardMarkup.getResizeKeyboard()),
                () -> assertFalse(resultReplyKeyboardMarkup.getOneTimeKeyboard()),
                () -> assertNotNull(resultReplyKeyboardMarkup.getKeyboard()),
                () -> assertEquals(1, resultReplyKeyboardMarkup.getKeyboard().size()),
                () -> assertEquals(1, resultReplyKeyboardMarkup.getKeyboard().get(0).size())
        );
        KeyboardButton resultKeyboardButton = resultReplyKeyboardMarkup.getKeyboard().get(0).get(0);
        Assertions.assertAll(
                () -> assertEquals(START_KEYBOARD, resultKeyboardButton.getText())
        );
    }
}
