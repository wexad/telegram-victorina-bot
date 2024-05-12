package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.ChatAdministratorRights;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.backend.model.answer.Answer;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.model.question.Question;
import uz.pdp.backend.service.answer_service.AnswerService;
import uz.pdp.backend.service.answer_service.AnswerServiceImpl;
import uz.pdp.backend.service.collection_service.CollectionService;
import uz.pdp.backend.service.collection_service.CollectionServiceImpl;
import uz.pdp.backend.service.question_service.QuestionService;
import uz.pdp.backend.service.question_service.QuestionServiceImpl;
import uz.pdp.backend.service.user_service.UserService;
import uz.pdp.backend.service.user_service.UserServiceImpl;
import uz.pdp.bean.BeanController;
import uz.pdp.bot.enums.bot_state.base.BaseState;
import uz.pdp.bot.enums.bot_state.child.CreateCollectionState;

import java.util.List;
import java.util.Objects;

public class CallBackQueryHandler extends BaseHandler {

    private final UserService userService = UserServiceImpl.getInstance();

    private final CollectionService collectionService = CollectionServiceImpl.getInstance();

    private final QuestionService questionService = QuestionServiceImpl.getInstance();

    private final AnswerService answerService = AnswerServiceImpl.getInstance();

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();

        myUser = getUserOrCreate(callbackQuery.from());

        System.out.println(myUser.getUserName() + " : " + data);

        if (Objects.equals(myUser.getSubState(), CreateCollectionState.CREATE_OR_ANOTHER.toString())) {
            switch (data) {

                case "ADD_QUESTION" -> {
                    myUser.setSubState(CreateCollectionState.ENTER_QUESTION.toString());
                    userService.update(myUser);
                    sendText(myUser.getChatId(), "Enter question please : ");
                }

                case "FINISH_COLLECTION" -> {
                    Collection lastCollection = collectionService.getLastCollectionUser(myUser);
                    lastCollection.setIsFinished(true);
                    collectionService.update(lastCollection);
                    myUser.setBaseState(BaseState.MAIN_STATE.toString());
                    myUser.setSubState(null);
                    userService.update(myUser);
                    sendText(myUser.getChatId(), "Collection build successfully! ");
                    BeanController.MESSAGE_HANDLER_THREAD_LOCAL.get().showMainMenu();
                }
            }
        }

        switch (data) {

            case "SHOW_COLLECTIONS" -> {
                List<Collection> userCollections = collectionService.getUserCollections(myUser);
                if (userCollections.isEmpty()) {
                    sendText(myUser.getChatId(), "You don't have any collections! ");
                } else {
                    showCollections(userCollections);
                }
            }

            case "CREATE_COLLECTION" -> {
                myUser.setBaseState(BaseState.CREATE_COLLECTION.toString());
                myUser.setSubState(CreateCollectionState.ENTER_NAME_OF_COLLECTION.toString());
                userService.update(myUser);
                sendText(myUser.getChatId(), "Please send name of new collection : ");
            }
        }


    }

    private void showCollections(List<Collection> userCollections) {
        myUser.setBaseState(BaseState.MY_COLLECTIONS.toString());
        userService.update(myUser);

        SendMessage showCollections = new SendMessage(myUser.getChatId(), "Your collections : ");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (Collection userCollection : userCollections) {
            InlineKeyboardButton button = new InlineKeyboardButton(userCollection.getName());
            button.callbackData(userCollection.getName());
            inlineKeyboardMarkup.addRow(button);
        }
        showCollections.replyMarkup(inlineKeyboardMarkup);
        SendResponse execute = bot.execute(showCollections);

        if (execute.isOk()) {
            System.out.println("Sent!");
        } else {
            System.out.println("Something wrong! ");
        }
        myUser.setBaseState(BaseState.MAIN_STATE.toString());
        myUser.setSubState(null);
        userService.update(myUser);
    }
}
