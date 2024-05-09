package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.enums.bot_state.BotState;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.service.collection_service.CollectionService;
import uz.pdp.backend.service.collection_service.CollectionServiceImpl;
import uz.pdp.backend.service.user_service.UserService;
import uz.pdp.backend.service.user_service.UserServiceImpl;

import java.util.List;

public class CallBackQueryHandler extends BaseHandler {

    private final UserService userService = UserServiceImpl.getInstance();

    private final CollectionService collectionService = CollectionServiceImpl.getInstance();

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();

        User user = callbackQuery.from();
        String data = callbackQuery.data();

        BotUser botUser = userService.getOrCreate(user);

        switch (data) {
            case "COLLECTIONS" -> {
                botUser.setBotState(BotState.MY_COLLECTIONS);
                List<Collection> userCollections = collectionService.getUserCollections(botUser);

                if (userCollections.isEmpty()) {
                    sendText(user.id(), "You don't have any collections of questions! ");
                    botUser.setBotState(BotState.MAIN);
                } else {
                    showCollections(botUser, userCollections);
                }
            }

            case "NEW_COLLECTION" -> {
                botUser.setBotState(BotState.COLLECTION_CREATING);
                sendText(botUser.getId(), "Please send new collection name : ");
            }
        }

    }

    private void showCollections(BotUser botUser, List<Collection> userCollections) {
        SendMessage collections = new SendMessage(botUser.getId(), "Your collections (choose one to see full questions and variations) : ");

        KeyboardButton[][] keyboardButtons = new KeyboardButton[userCollections.size() + 1][1];

        for (int i = 0; i < userCollections.size(); i++) {
            KeyboardButton keyboardButton = new KeyboardButton(userCollections.get(i).getName());
            keyboardButtons[i][0] = keyboardButton;
        }
        KeyboardButton button = new KeyboardButton("Back");

        keyboardButtons[userCollections.size()][0] = button;

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButtons);

        collections.replyMarkup(replyKeyboardMarkup);

        bot.execute(collections);
    }

    private void sendText(Long id, String text) {
        SendMessage sendMessage = new SendMessage(id, text);
        bot.execute(sendMessage);
    }
}
