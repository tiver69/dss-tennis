package com.dss.tennis.tournament.bot.helper;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
public class KeyboardMarkupHelper {

    public static final String YES_CALLBACK_DATA = "yes";
    public static final String NO_CALLBACK_DATA = "no";
    public static final String CREATE_TOURNAMENT_CALLBACK_DATA = "createTournament";
    public static final String UPDATE_TOURNAMENT_CALLBACK_DATA = "updateTournament";
    public static final String ROUND_TYPE_CALLBACK_DATA = "roundType";
    public static final String ELIMINATION_TYPE_CALLBACK_DATA = "eliminationType";

    public static final Map<String, String> CALLBACK_DATA_TO_TEXT = new HashMap<>();

    public KeyboardMarkupHelper() {
        CALLBACK_DATA_TO_TEXT.put(YES_CALLBACK_DATA, "Yes");
        CALLBACK_DATA_TO_TEXT.put(NO_CALLBACK_DATA, "No");
        CALLBACK_DATA_TO_TEXT.put(CREATE_TOURNAMENT_CALLBACK_DATA, "Create Tournament");
        CALLBACK_DATA_TO_TEXT.put(UPDATE_TOURNAMENT_CALLBACK_DATA, "Update Tournament");
        CALLBACK_DATA_TO_TEXT.put(ROUND_TYPE_CALLBACK_DATA, "Round");
        CALLBACK_DATA_TO_TEXT.put(ELIMINATION_TYPE_CALLBACK_DATA, "Elimination");
    }

    public InlineKeyboardMarkup getTournamentActionMarkup() {
        return getInlineMarkup(CREATE_TOURNAMENT_CALLBACK_DATA, UPDATE_TOURNAMENT_CALLBACK_DATA);
    }

    public InlineKeyboardMarkup getTournamentTypeMarkup() {
        return getInlineMarkup(ROUND_TYPE_CALLBACK_DATA, ELIMINATION_TYPE_CALLBACK_DATA);
    }

    public InlineKeyboardMarkup getYesNoMarkup() {
        return getInlineMarkup(YES_CALLBACK_DATA, NO_CALLBACK_DATA);
    }

    private InlineKeyboardMarkup getInlineMarkup(String... callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        Arrays.stream(callbackData).forEach(callback -> {
            String callbackText = CALLBACK_DATA_TO_TEXT.get(callback);
            InlineKeyboardButton callbackButton = new InlineKeyboardButton().setText(callbackText);
            callbackButton.setCallbackData(callback);
            keyboardButtonsRow.add(callbackButton);
        });
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow));
        return inlineKeyboardMarkup;
    }
}
