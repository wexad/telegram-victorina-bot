package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
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

import java.util.List;
import java.util.Objects;

public class MessageHandler extends BaseHandler {

    private final UserService userService = UserServiceImpl.getInstance();

    private final CollectionService collectionService = CollectionServiceImpl.getInstance();

    private final QuestionService questionService = QuestionServiceImpl.getInstance();

    private final VariationService variationService = VariationServiceImpl.getInstance();

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Chat chat = message.chat();
        User from = message.from();
        String text = message.text();

        BotUser botUser = userService.getOrCreate(from);


        System.out.println(botUser.getUserName() + " : " + text);


        if (Objects.equals(text, "/start")) {
            SendMessage sendMessage = new SendMessage(chat.id(), "Hello! " + botUser.getUserName());
            bot.execute(sendMessage);
        }

//        if (isFromBot(message)) {
//            switch (botUser.getBotState()) {
//                case MAIN -> {
//                    SendMessage sendMessage = new SendMessage(chat.id(), "Menu : ");
//                    InlineKeyboardButton[][] buttons = new InlineKeyboardButton[1][2];
//
//                    InlineKeyboardButton button1 = new InlineKeyboardButton("My collections");
//                    button1.callbackData("COLLECTIONS");
//
//                    InlineKeyboardButton button2 = new InlineKeyboardButton("Create a new collections");
//                    button2.callbackData("NEW_COLLECTION");
//
//                    buttons[0][0] = button1;
//                    buttons[0][1] = button2;
//
//                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(buttons);
//
//                    sendMessage.replyMarkup(inlineKeyboardMarkup);
//
//                    bot.execute(sendMessage);
//                }
//                case COLLECTION_CREATING -> {
//                    Collection collection = new Collection(text, botUser.getId());
//
//                    collectionService.add(collection);
//
//                    sentText(botUser.getId(), "Please add at least one question with variations : ");
//                }
//                case MY_COLLECTIONS -> {
//                    if (text.equals("Back")) {
//                        botUser.setBotState(BotState.MAIN);
//                    } else {
//                        Collection collection = collectionService.getCollectionByName(text);
//
//                        StringBuilder stringBuilder = new StringBuilder("Collection : " + collection.getName());
//                        List<Question> questionsByCollectionId = questionService.getQuestionsByCollectionId(collection.getId());
//
//                        int count = 1;
//                        for (Question question : questionsByCollectionId) {
//                            stringBuilder.append("\n").append("Question ").append(count++).append(" : ").append(question.getText());
//                            List<Variation> variationsByQuestionId = variationService.getVariationsByQuestionId(question.getId());
//                            int count1 = 1;
//                            for (Variation variation : variationsByQuestionId) {
//                                stringBuilder.append("\t").append("Variation ").append(count1++).append(" : ").append(variation.getAnswer());
//                            }
//                        }
//
//                        sentText(botUser.getId(), stringBuilder.toString());
//                        botUser.setBotState(BotState.MAIN);
//                    }
//                }
//            }
//        }
    }

    private void sentText(Long id, String text) {
        SendMessage sendMessage = new SendMessage(id, text);
        bot.execute(sendMessage);
    }

    private boolean isFromBot(Message message) {
        return message.chat().title() == null;
    }
}
