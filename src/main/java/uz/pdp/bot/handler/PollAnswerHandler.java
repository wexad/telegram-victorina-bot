package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;

public class PollAnswerHandler extends BaseHandler {
    @Override
    public void handle(Update update) {
        PollAnswer pollAnswer = update.pollAnswer();

        String pollId = pollAnswer.pollId();

        User user = pollAnswer.user();

        Integer answerIndex = pollAnswer.optionIds()[0];

        Chat chat = pollAnswer.voterChat();

    }
}
