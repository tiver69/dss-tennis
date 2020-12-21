package com.dss.tennis.tournament.bot.command;

import com.dss.tennis.tournament.bot.service.KeyboardMarkupService;
import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import com.dss.tennis.tournament.tables.dto.CreateTournamentDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

public class CreateTournamentCommand implements DssBotCommand {

    private final DssTennisUserStateCache userStateCache;
    private final KeyboardMarkupService keyboardMarkupService;
    private final Map<BotState, String> messageQuestionToState = new HashMap<>();

    public CreateTournamentCommand() {
        messageQuestionToState.put(BotState.ASK_NAME, "What is tournament name?");
        messageQuestionToState.put(BotState.ASK_TYPE, "What is tournament type?");
        messageQuestionToState.put(BotState.ASK_PLAYERS, "Enter participants:");
        messageQuestionToState.put(BotState.ASK_ADDITIONAL_PLAYERS, "Would you like to add more?");
        messageQuestionToState.put(BotState.CONFIRM_TOURNAMENT, "We are going to save %s %s tournament with %d players. Pressed?");
        messageQuestionToState.put(BotState.SAVE_TOURNAMENT, "Tournament was saved. Lets play now!");
        messageQuestionToState.put(BotState.ROLLBACK_TOURNAMENT, "Canceling changes");
        userStateCache = DssTennisUserStateCache.getInstance();
        keyboardMarkupService = new KeyboardMarkupService();
    }

    @Override
    public SendMessage execute(int userId, Message message) {
        BotState currentBotState = userStateCache.getUsersCurrentBotState(userId);
        CreateTournamentDTO createTournamentDTO = userStateCache.getUserCreateTournamentDTO(userId);
        SendMessage replyToUser = new SendMessage(message.getChatId(), messageQuestionToState.get(currentBotState));

        if (BotState.ASK_NAME.equals(currentBotState)) {
            userStateCache.setUsersCurrentBotState(userId, BotState.ASK_TYPE);
        }
        if (BotState.ASK_TYPE.equals(currentBotState)) {
            createTournamentDTO.setName(message.getText());
            replyToUser.setReplyMarkup(keyboardMarkupService.getTournamentTypeMarkup());
            userStateCache.setUsersCurrentBotState(userId, BotState.ASK_PLAYERS);
        }
        if (BotState.ASK_PLAYERS.equals(currentBotState)) {
            userStateCache.setUsersCurrentBotState(userId, BotState.ASK_ADDITIONAL_PLAYERS);
        }
        if (BotState.ASK_ADDITIONAL_PLAYERS.equals(currentBotState)) {
            //todo: refactor this mess
            if (createTournamentDTO.getPlayers() == null) createTournamentDTO.setPlayers(new ArrayList<>());
            addPlayers(message.getText(), createTournamentDTO.getPlayers());
            replyToUser.setReplyMarkup(keyboardMarkupService.getYesNoMarkup());
        }
        if (BotState.CONFIRM_TOURNAMENT.equals(currentBotState)) {
            replyToUser.setText(String.format(messageQuestionToState.get(currentBotState),
                    createTournamentDTO.getName(),
                    createTournamentDTO.getType().toString().toLowerCase(),
                    createTournamentDTO.getPlayers().size()));
            replyToUser.setReplyMarkup(keyboardMarkupService.getYesNoMarkup());
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
