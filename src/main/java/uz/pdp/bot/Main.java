package uz.pdp.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import uz.pdp.backend.service.collection_service.CollectionService;
import uz.pdp.backend.service.collection_service.CollectionServiceImpl;
import uz.pdp.bot.handler.BotManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final String BOT_TOKEN = "6967161720:AAHruHzw1jD3g0PgpL8akuf3NizFrXCJels";

    public static final ThreadLocal<BotManager> BOT_MAIN_HANDLER_THREAD_LOCAL = ThreadLocal.withInitial(BotManager::new);

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(BOT_TOKEN);
        bot.setUpdatesListener(
                list -> {
                    for (Update update : list) {
                        CompletableFuture.runAsync(() -> BOT_MAIN_HANDLER_THREAD_LOCAL.get().manage(update), EXECUTOR_SERVICE);
                    }
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }, Throwable::printStackTrace
        );


    }
}
