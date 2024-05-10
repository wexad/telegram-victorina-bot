package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.enums.bot_state.BotState;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.model.question.Question;
import uz.pdp.backend.model.variation.Variation;
import uz.pdp.backend.service.collection_service.CollectionService;
import uz.pdp.backend.service.collection_service.CollectionServiceImpl;
import uz.pdp.backend.service.question_service.QuestionService;
import uz.pdp.backend.service.question_service.QuestionServiceImpl;
import uz.pdp.backend.service.user_service.UserService;
import uz.pdp.backend.service.user_service.UserServiceImpl;
import uz.pdp.backend.service.variation_service.VariationService;
import uz.pdp.backend.service.variation_service.VariationServiceImpl;

import java.util.Arrays;
import java.util.List;

public class CallBackQueryHandler extends BaseHandler {

    private final UserService userService = UserServiceImpl.getInstance();

    private final CollectionService collectionService = CollectionServiceImpl.getInstance();

    private final QuestionService questionService = QuestionServiceImpl.getInstance();

    private final VariationService variationService = VariationServiceImpl.getInstance();

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();

        User user = callbackQuery.from();
        String data = callbackQuery.data();

        BotUser botUser = userService.getOrCreate(user);

        switch (data) {
            case "COLLECTIONS" -> {
                botUser.setBotState(BotState.MY_COLLECTIONS);
                System.out.println(botUser.getBotState());
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
                sendText(botUser.getChatId(), "Please send new collection name : ");
                System.out.println(botUser.getBotState());
            }
        }

        if (botUser.getBotState().equals(BotState.MY_COLLECTIONS)) {
            if (data.equals("Back")) {

                botUser.setBotState(BotState.MAIN);
                System.out.println(botUser.getBotState());
            } else {
                Collection collection = collectionService.getCollectionByName(data);
                showCollection(collection, botUser);
            }
        }

    }

    private void showCollection(Collection collection, BotUser botUser) {
        StringBuilder stringBuilder = new StringBuilder("Collection : " + collection.getName());
        List<Question> questionsByCollectionId = questionService.getQuestionsByCollectionId(collection.getId());

        int count = 1;
        for (Question question : questionsByCollectionId) {
            stringBuilder.append("\n").append("Question ").append(count++).append(" : ").append(question.getText());
            List<Variation> variationsByQuestionId = variationService.getVariationsByQuestionId(question.getId());
            int count1 = 1;
            for (Variation variation : variationsByQuestionId) {
                stringBuilder.append("\t").append("Variation ").append(count1++).append(" : ").append(variation.getAnswer());
            }
        }

        sendText(botUser.getChatId(), stringBuilder.toString());
        botUser.setBotState(BotState.MAIN);
    }

    private void showCollections(BotUser botUser, List<Collection> userCollections) {
        SendMessage collections = new SendMessage(botUser.getId(), "Your collections (choose one to see full questions and variations) : ");

        InlineKeyboardButton[][] inlineKeyboardButtons = new InlineKeyboardButton[userCollections.size() + 1][1];

        for (int i = 0; i < userCollections.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton(userCollections.get(i).getName());
            keyboardButton.callbackData(userCollections.get(i).getName());

            inlineKeyboardButtons[i][0] = keyboardButton;
        }
        InlineKeyboardButton button = new InlineKeyboardButton("Back");
        button.callbackData("Back");

        inlineKeyboardButtons[userCollections.size()][0] = button;

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineKeyboardButtons);

        collections.replyMarkup(inlineKeyboardMarkup);

        bot.execute(collections);
    }

    private void sendText(Long id, String text) {
        SendMessage sendMessage = new SendMessage(id, text);
        bot.execute(sendMessage);
    }
}
