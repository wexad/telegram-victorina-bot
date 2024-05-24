package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.*;
import uz.pdp.bean.BeanController;

import java.util.Objects;

public class BotManager {

    public void manage(Update update) {
        Message message = update.message();

        CallbackQuery callbackQuery = update.callbackQuery();

        PollAnswer pollAnswer = update.pollAnswer();

        Poll poll = update.poll();

        if (Objects.nonNull(message)) {
            BeanController.MESSAGE_HANDLER_THREAD_LOCAL.get().handle(update);
        } else if (Objects.nonNull(callbackQuery)) {
            BeanController.CALL_BACK_QUERY_HANDLER_THREAD_LOCAL.get().handle(update);
        } else if (Objects.nonNull(poll)) {
            BeanController.POLL_ANSWER_HANDLER_THREAD_LOCAL.get().handle(update);
        }
    }

}
