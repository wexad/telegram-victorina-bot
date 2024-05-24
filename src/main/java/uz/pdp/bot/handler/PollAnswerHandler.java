package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.*;
import uz.pdp.backend.model.poll_back.PollBack;
import uz.pdp.backend.model.result.Result;

import java.util.Objects;

public class PollAnswerHandler extends BaseHandler {
    @Override
    public void handle(Update update) {
        System.out.println("ENTER to PollAnswerHandler");

        PollAnswer pollAnswer = update.pollAnswer();
        myGroup = getGroupOrCreate(pollAnswer.voterChat());
        String pollId = pollAnswer.pollId();
        User user = pollAnswer.user();
        Integer answerIndex = pollAnswer.optionIds()[0];

        PollBack pollBack = pollBackService.getById(pollId);

        if (Objects.equals(pollBack.getTrueIndex(), answerIndex)) {
            Result result = resultService.getByUsername(user.username());

            if (result == null) {
                result = new Result(user.username(), myGroup.getChatId(), 0);

                resultService.add(result);
                resultService.update(result);
            }

            result.setCount(result.getCount() + 1);

            resultService.update(result);

            sendText(user.id(), "Correct! ✔");
        } else {
            sendText(user.id(), "Incorrect! ❌");
        }
    }
}
