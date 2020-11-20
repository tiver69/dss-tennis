package com.dss.tennis.tournament.bot;

import com.dss.tennis.tournament.bot.service.MainMenuService;
import com.dss.tennis.tournament.bot.state.BotState;
import com.dss.tennis.tournament.bot.state.BotStateHelper;
import com.dss.tennis.tournament.bot.state.cache.DssTennisUserStateCache;
import com.dss.tennis.tournament.tables.model.v1.TournamentType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.dss.tennis.tournament.bot.service.KeyboardMarkupService.*;

public class DssTennisBotFacade {

    private final MainMenuService mainMenuService;
    private final DssTennisUserStateCache userStateCache;
    private final BotStateHelper botStateHelper;

    public DssTennisBotFacade() {
        this.mainMenuService = new MainMenuService();
        this.userStateCache = DssTennisUserStateCache.getInstance();
        this.botStateHelper = new BotStateHelper();
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
//            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
//                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }

        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
//            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
//                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String messageText = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;

        switch (messageText) {
            case "/start":
                botState = BotState.INIT;
                break;
            default:
                botState = userStateCache.getUsersCurrentBotState(userId);
                break;
        }

        userStateCache.setUsersCurrentBotState(userId, botState);
        return botStateHelper.processInputMessage(botState, message);
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final int userId = buttonQuery.getFrom().getId();
        BotState botState;

        switch (buttonQuery.getData()) {
            case CREATE_TOURNAMENT_CALLBACK_DATA:
                botState = BotState.ASK_NAME;
                break;
            case UPDATE_TOURNAMENT_CALLBACK_DATA:
                botState = BotState.ADD_MATCH;
                break;
            case ROUND_TOURNAMENT_TYPE_CALLBACK_DATA:
                //todo remove it from here
                userStateCache.getUserCreateTournamentDTO(userId).setType(TournamentType.ROUND);
                botState = userStateCache.getUsersCurrentBotState(userId);
                break;
            case ELIMINATION_TOURNAMENT_CALLBACK_DATA:
                //todo remove it from here
                botState = userStateCache.getUsersCurrentBotState(userId);
                userStateCache.getUserCreateTournamentDTO(userId).setType(TournamentType.ELIMINATION);
                break;
            case YES_CALLBACK_DATA:
                botState = processYesCallbackData(userId);
                break;
            case NO_CALLBACK_DATA:
                botState = processNoCallbackData(userId);
                break;
            default:
                botState = userStateCache.getUsersCurrentBotState(userId);
                break;
        }

        userStateCache.setUsersCurrentBotState(userId, botState);
        return botStateHelper.processInputMessage(botState, userId, buttonQuery.getMessage());
    }

    private BotState processYesCallbackData(int userId) {
        BotState currentBotState = userStateCache.getUsersCurrentBotState(userId);
        switch (currentBotState) {
            case ASK_ADDITIONAL_PLAYERS:
                return BotState.ASK_PLAYERS;
            case CONFIRM_TOURNAMENT:
                return BotState.SAVE_TOURNAMENT;
            default:
                return currentBotState;
        }
    }

    private BotState processNoCallbackData(int userId) {
        BotState currentBotState = userStateCache.getUsersCurrentBotState(userId);
        switch (currentBotState) {
            case ASK_ADDITIONAL_PLAYERS:
                return BotState.CONFIRM_TOURNAMENT;
            case CONFIRM_TOURNAMENT:
                return BotState.MAIN_MENU;
            default:
                return currentBotState;
        }
    }
}
