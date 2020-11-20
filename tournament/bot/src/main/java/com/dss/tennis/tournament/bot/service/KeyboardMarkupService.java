package com.dss.tennis.tournament.bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//todo refactor here
public class KeyboardMarkupService {

    public static final String YES_CALLBACK_DATA = "yes";
    public static final String NO_CALLBACK_DATA = "no";
    public static final String CREATE_TOURNAMENT_CALLBACK_DATA = "createTournament";
    public static final String UPDATE_TOURNAMENT_CALLBACK_DATA = "updateTournament";
    public static final String ROUND_TOURNAMENT_TYPE_CALLBACK_DATA = "roundTournamentType";
    public static final String ELIMINATION_TOURNAMENT_CALLBACK_DATA = "eliminationTournamentType";

    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        //Every button must have callBackData, or else not work !
        InlineKeyboardButton buttonCreateTournament = new InlineKeyboardButton().setText("Create Tournament");
        buttonCreateTournament.setCallbackData(CREATE_TOURNAMENT_CALLBACK_DATA);
        InlineKeyboardButton buttonUpdateTournament = new InlineKeyboardButton().setText("Update Tournament");
        buttonUpdateTournament.setCallbackData(UPDATE_TOURNAMENT_CALLBACK_DATA);

        List<InlineKeyboardButton> keyboardButtonsRow = Arrays.asList(buttonCreateTournament, buttonUpdateTournament);
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow));
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getTournamentTypeMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        //Every button must have callBackData, or else not work !
        InlineKeyboardButton roundTypeButton = new InlineKeyboardButton().setText("Round");
        roundTypeButton.setCallbackData(ROUND_TOURNAMENT_TYPE_CALLBACK_DATA);
        InlineKeyboardButton eliminationTypeButton = new InlineKeyboardButton().setText("Elimination");
        eliminationTypeButton.setCallbackData(ELIMINATION_TOURNAMENT_CALLBACK_DATA);

        List<InlineKeyboardButton> keyboardButtonsRow = Arrays.asList(roundTypeButton, eliminationTypeButton);
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow));
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getYesNoMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        //Every button must have callBackData, or else not work !
        InlineKeyboardButton noButton = new InlineKeyboardButton().setText("No");
        noButton.setCallbackData(NO_CALLBACK_DATA);
        InlineKeyboardButton yesButton = new InlineKeyboardButton().setText("Yes");
        yesButton.setCallbackData(YES_CALLBACK_DATA);

        List<InlineKeyboardButton> keyboardButtonsRow = Arrays.asList(yesButton, noButton);
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow));
        return inlineKeyboardMarkup;
    }
}
