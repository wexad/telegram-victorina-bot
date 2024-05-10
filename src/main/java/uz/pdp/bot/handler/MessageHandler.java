package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.bot.enums.bot_state.child.MainState;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.service.collection_service.CollectionService;
import uz.pdp.backend.service.collection_service.CollectionServiceImpl;
import uz.pdp.backend.service.question_service.QuestionService;
import uz.pdp.backend.service.question_service.QuestionServiceImpl;
import uz.pdp.backend.service.user_service.UserService;
import uz.pdp.backend.service.user_service.UserServiceImpl;
import uz.pdp.backend.service.variation_service.VariationService;
import uz.pdp.backend.service.variation_service.VariationServiceImpl;

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

//
//        if (Objects.equals(text, "/start")) {
//            botUser.setBotState(MainState.MAIN);
//            SendMessage sendMessage = new SendMessage(chat.id(), "Hello! " + botUser.getUserName());
//            bot.execute(sendMessage);
//        }
//
//        if (isFromBot(message)) {
//            if (Objects.requireNonNull(botUser.getBotState()) == MainState.MAIN) {
//                SendMessage sendMessage = new SendMessage(chat.id(), "Menu : ");
//                InlineKeyboardButton[][] buttons = new InlineKeyboardButton[1][2];
//
//                InlineKeyboardButton button1 = new InlineKeyboardButton("My collections");
//                button1.callbackData("COLLECTIONS");
//
//                InlineKeyboardButton button2 = new InlineKeyboardButton("Create a new collections");
//                button2.callbackData("NEW_COLLECTION");
//
//                buttons[0][0] = button1;
//                buttons[0][1] = button2;
//
//                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(buttons);
//
//                sendMessage.replyMarkup(inlineKeyboardMarkup);
//
//                bot.execute(sendMessage);
//            } else if (botUser.getBotState() == MainState.COLLECTION_CREATING) {
//                System.out.println("Enter");
//                Collection collection = new Collection(text, botUser.getChatId(), false);
//
//                collectionService.add(collection);
//
//                sendText(botUser.getChatId(), "Please add at least one question with variations : ");
//
//                botUser.setBotState(MainState.QUESTION_ADD);
//
//                InlineKeyboardButton button1 = new InlineKeyboardButton("Add question");
//                button1.callbackData();
//
//                InlineKeyboardButton button2 = new InlineKeyboardButton("Finish");
//                button2.callbackData();
//
//                InlineKeyboardButton button3 = new InlineKeyboardButton("Cancel");
//                button3.callbackData();
//
//                InlineKeyboardButton[][] buttons = new InlineKeyboardButton[2][2];
//
//                buttons[0][0] = button1;
//                buttons[0][1] = button2;
//                buttons[1][0] = button3;
//
//                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(buttons);
//
//                SendMessage sendMessage = new SendMessage(botUser.getChatId(), "Choose : ");
//
//                sendMessage.replyMarkup(inlineKeyboardMarkup);
//
//                bot.execute(sendMessage);
//            }
//        }
    }

    private void sendText(Long id, String text) {
        SendMessage sendMessage = new SendMessage(id, text);
        bot.execute(sendMessage);
    }

    private boolean isFromBot(Message message) {
        return message.chat().title() == null;
    }
}
