package uz.pdp.bot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.group.Group;
import uz.pdp.backend.service.answer_service.AnswerService;
import uz.pdp.backend.service.collection_service.CollectionService;
import uz.pdp.backend.service.game_service.GameService;
import uz.pdp.backend.service.group_service.GroupService;
import uz.pdp.backend.service.question_service.QuestionService;
import uz.pdp.backend.service.result_service.ResultService;
import uz.pdp.backend.service.user_service.UserService;
import uz.pdp.bean.BeanController;
import uz.pdp.bot.Main;

public abstract class BaseHandler {

    protected TelegramBot bot;
    protected Group myGroup;
    protected BotUser myUser;
    protected UserService userService;
    protected CollectionService collectionService;
    protected GameService gameService;
    protected GroupService groupService;
    protected QuestionService questionService;
    protected ResultService resultService;
    protected AnswerService answerService;

    public BaseHandler() {
        this.bot = new TelegramBot(Main.BOT_TOKEN);
        this.userService = BeanController.USER_SERVICE_THREAD_LOCAL.get();
        this.collectionService = BeanController.COLLECTION_SERVICE_THREAD_LOCAL.get();
        this.gameService = BeanController.GAME_SERVICE_THREAD_LOCAL.get();
        this.groupService = BeanController.GROUP_SERVICE_THREAD_LOCAL.get();
        this.questionService = BeanController.QUESTION_SERVICE_THREAD_LOCAL.get();
        this.resultService = BeanController.RESULT_SERVICE_THREAD_LOCAL.get();
        this.answerService = BeanController.VARIATION_SERVICE_THREAD_LOCAL.get();
    }

    public abstract void handle(Update update);

    protected Group getGroupOrCreate(Chat chat) {
        Group myGroup = groupService.getById(chat.id());
        if (myGroup == null) {
            myGroup = Group.builder()
                    .title(chat.title())
                    .chatId(chat.id())
                    .userName(chat.username())
                    .build();
            groupService.add(myGroup);
        }

        return myGroup;
    }

    protected BotUser getUserOrCreate(User from) {
        BotUser myUser = userService.getUserById(from.id());
        if (myUser == null) {
            myUser = BotUser.builder()
                    .chatId(from.id())
                    .userName(from.username())
                    .firstName(from.firstName())
                    .lastName(from.lastName())
                    .build();
            userService.add(myUser);
        }
        return myUser;
    }

    protected void sendText(Long id, String text) {
        SendMessage sendMessage = new SendMessage(id, text);
        bot.execute(sendMessage);
    }
}
