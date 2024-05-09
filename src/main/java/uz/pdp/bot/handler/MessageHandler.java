package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.model.variation.Variation;

import java.util.Objects;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Chat chat = message.chat();

        String text = message.text();

        System.out.println("Group : " + chat.title());
        System.out.println("From : " + message.from().username());
        System.out.println("Text : " + text);

        if (Objects.equals(text, "/start")) {

            SendMessage sendMessage = new SendMessage(chat.id(), "Hello! ");
            bot.execute(sendMessage);
        }
    }

    private String[] getOptions(Variation... variations) {
        String[] options = new String[variations.length];
        for (int i = 0; i < variations.length; i++) {
            options[i] = variations[i].getAnswer();
        }

        return options;
    }
}
