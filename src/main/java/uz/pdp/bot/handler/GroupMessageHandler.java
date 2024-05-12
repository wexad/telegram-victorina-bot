package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

public class GroupMessageHandler extends BaseHandler{
    @Override
    public void handle(Update update) {
        Message message = update.message();

        Chat chat = message.chat();


    }
}
