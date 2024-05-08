package uz.pdp.bot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import uz.pdp.bot.Main;

public abstract class BaseHandler {

    protected TelegramBot bot;

    public BaseHandler() {
        this.bot = new TelegramBot(Main.BOT_TOKEN);
    }

    public abstract void handle(Update update);
}
