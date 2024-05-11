package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
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

        myUser = getUserOrCreate(from);
        if (Objects.nonNull(text)) {
            System.out.println(myUser.getUserName() + " : " + text);

            if (Objects.equals(text, "/start")) {
                sendText(chat.id(), "Hello! " + myUser.getUserName());
            }
        }


    }

    private void sendText(Long id, String text) {
        SendMessage sendMessage = new SendMessage(id, text);
        bot.execute(sendMessage);
    }

    private boolean isFromBot(Message message) {
        return message.chat().title() == null;
    }
}
