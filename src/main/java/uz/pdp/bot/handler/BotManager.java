package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.Update;

import java.util.Objects;

public class BotManager {

    public void manage(Update update) {
        Message message = update.message();

        CallbackQuery callbackQuery = update.callbackQuery();

        PollAnswer pollAnswer = update.pollAnswer();


        if (Objects.nonNull(message)) {
            MessageHandler messageHandler = new MessageHandler();
            messageHandler.handle(update);

        } else if (Objects.nonNull(callbackQuery)) {
            CallBackQueryHandler callBackQueryHandler = new CallBackQueryHandler();
            callBackQueryHandler.handle(update);

        } else if (Objects.nonNull(pollAnswer)) {
            PollAnswerHandler pollAnswerHandler = new PollAnswerHandler();
            pollAnswerHandler.handle(update);
        }
    }
}
