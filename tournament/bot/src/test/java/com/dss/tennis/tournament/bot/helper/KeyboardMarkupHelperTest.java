package com.dss.tennis.tournament.bot.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.dss.tennis.tournament.bot.helper.KeyboardMarkupHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KeyboardMarkupHelperTest {

    private KeyboardMarkupHelper testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new KeyboardMarkupHelper();
    }

    @Test
    public void shouldReturnYesNoMarkupWithTwoButtons() {
        InlineKeyboardMarkup result = testInstance.getYesNoMarkup();
        performCheckForTwoButtons(result, CALLBACK_DATA_TO_TEXT.get(YES_CALLBACK_DATA), YES_CALLBACK_DATA,
                CALLBACK_DATA_TO_TEXT.get(NO_CALLBACK_DATA), NO_CALLBACK_DATA);
    }

    @Test
    public void shouldReturnTournamentMarkupWithTwoButtons() {
        InlineKeyboardMarkup result = testInstance.getTournamentActionMarkup();
        performCheckForTwoButtons(result, CALLBACK_DATA_TO_TEXT.get(CREATE_TOURNAMENT_CALLBACK_DATA), CREATE_TOURNAMENT_CALLBACK_DATA,
                CALLBACK_DATA_TO_TEXT.get(UPDATE_TOURNAMENT_CALLBACK_DATA), UPDATE_TOURNAMENT_CALLBACK_DATA);
    }

    @Test
    public void shouldReturnTournamentTypeMarkupWithTwoButtons() {
        InlineKeyboardMarkup result = testInstance.getTournamentTypeMarkup();
        performCheckForTwoButtons(result, CALLBACK_DATA_TO_TEXT.get(ROUND_TYPE_CALLBACK_DATA), ROUND_TYPE_CALLBACK_DATA,
                CALLBACK_DATA_TO_TEXT.get(ELIMINATION_TYPE_CALLBACK_DATA), ELIMINATION_TYPE_CALLBACK_DATA);
    }

    private void performCheckForTwoButtons(InlineKeyboardMarkup result, String buttonOneText, String buttonOneCallback, String buttonTwoText, String buttonTwoCallback) {
        Assertions.assertAll(
                () -> assertNotNull(result.getKeyboard()),
                () -> assertEquals(1, result.getKeyboard().size()),
                () -> assertEquals(2, result.getKeyboard().get(0).size())
        );
        InlineKeyboardButton button1 = result.getKeyboard().get(0).get(0);
        InlineKeyboardButton button2 = result.getKeyboard().get(0).get(1);

        Assertions.assertAll(
                () -> assertEquals(buttonOneText, button1.getText()),
                () -> assertEquals(buttonOneCallback, button1.getCallbackData()),
                () -> assertEquals(buttonTwoText, button2.getText()),
                () -> assertEquals(buttonTwoCallback, button2.getCallbackData())
        );
    }
}
