package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPoll;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.backend.model.question.Question;
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

            Question question = new Question(null, "Hello? ");
            Variation variation1 = new Variation(question.getId(), "Hello");
            Variation variation2 = new Variation(question.getId(), "Hi");
            Variation variation3 = new Variation(question.getId(), "Ola");
            Variation variation4 = new Variation(question.getId(), "Gracia");


            SendPoll sendPoll = new SendPoll(chat.id(), question.getFullQuestion(), getOptions(variation1, variation2, variation3, variation4));

            bot.execute(sendPoll);
        }
    }

    private String[] getOptions(Variation... variations) {
        String[] options = new String[variations.length];
        for (int i = 0; i < variations.length; i++) {
            options[i] = variations[i].getFullVariation();
        }

        return options;
    }
}
