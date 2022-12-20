package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.helper.KeyboardMarkupHelper;
import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CreateTournamentCommand implements DssBotCommand {

    @Autowired
    private DssTennisUserStateCache userStateCache;
    @Autowired
    private KeyboardMarkupHelper keyboardMarkupHelper;

    public static final Map<BotState, String> STATE_TO_MESSAGE_MAP = new HashMap<>();

    static {
        STATE_TO_MESSAGE_MAP.put(BotState.ASK_NAME, "What is tournament name?");
        STATE_TO_MESSAGE_MAP.put(BotState.ASK_TYPE, "What is tournament type?");
        STATE_TO_MESSAGE_MAP.put(BotState.ASK_PLAYERS, "Enter participants:");
        STATE_TO_MESSAGE_MAP.put(BotState.ASK_ADDITIONAL_PLAYERS, "Would you like to add more?");
        STATE_TO_MESSAGE_MAP.put(BotState.CONFIRM_TOURNAMENT, "We are going to save %s %s tournament with %d players. Pressed?");
        STATE_TO_MESSAGE_MAP.put(BotState.SAVE_TOURNAMENT, "Tournament was saved. Lets play now!");
        STATE_TO_MESSAGE_MAP.put(BotState.ROLLBACK_TOURNAMENT, "Canceling changes");
    }

    @Override
    public SendMessage execute(int userId, Message message) {
        BotState currentBotState = userStateCache.getUsersCurrentBotState(userId);
        CreateTournamentDTO createTournamentDTO = userStateCache.getUserCreateTournamentDTO(userId);
        SendMessage replyToUser = new SendMessage(message.getChatId(), STATE_TO_MESSAGE_MAP.get(currentBotState));

        if (BotState.ASK_NAME.equals(currentBotState)) {
            userStateCache.setUsersCurrentBotState(userId, BotState.ASK_TYPE);
        }
        if (BotState.ASK_TYPE.equals(currentBotState)) {
            createTournamentDTO.setName(message.getText());
            replyToUser.setReplyMarkup(keyboardMarkupHelper.getTournamentTypeMarkup());
            userStateCache.setUsersCurrentBotState(userId, BotState.ASK_PLAYERS);
        }
        if (BotState.ASK_PLAYERS.equals(currentBotState)) {
            userStateCache.setUsersCurrentBotState(userId, BotState.ASK_ADDITIONAL_PLAYERS);
        }
        if (BotState.ASK_ADDITIONAL_PLAYERS.equals(currentBotState)) {
            addPlayers(message.getText(), createTournamentDTO.getPlayers());
            replyToUser.setReplyMarkup(keyboardMarkupHelper.getYesNoMarkup());
        }
        if (BotState.CONFIRM_TOURNAMENT.equals(currentBotState)) {
            replyToUser.setText(String.format(STATE_TO_MESSAGE_MAP.get(currentBotState),
                    createTournamentDTO.getName(),
                    createTournamentDTO.getType().toString().toLowerCase(),
                    createTournamentDTO.getPlayers().size()));
            replyToUser.setReplyMarkup(keyboardMarkupHelper.getYesNoMarkup());
        }
        if (BotState.SAVE_TOURNAMENT.equals(currentBotState)) {
            //todo: what status should be here
            //perform api call to save here
            userStateCache.setUsersCurrentBotState(userId, BotState.MAIN_MENU);
        }
        return replyToUser;
    }

    private void addPlayers(String message, List<String> players) {
        Arrays.stream(message.trim().split("\n")).filter(player -> !player.isEmpty()).forEach(players::add);
    }
}
