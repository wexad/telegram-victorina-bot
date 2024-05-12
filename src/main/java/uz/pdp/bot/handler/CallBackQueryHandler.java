package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
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

        if (myUser.getBaseState().equals(BaseState.MY_COLLECTIONS.toString())) {

        }

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
                    showCollections(myUser, userCollections);
                }
            }

            case "CREATE_COLLECTION" -> {
                myUser.setBaseState(BaseState.CREATE_COLLECTION.toString());
                myUser.setSubState(CreateCollectionState.ENTER_NAME_OF_COLLECTION.toString());
                userService.update(myUser);
                sendText(myUser.getChatId(), "Please send name of new collection : ");
            }
        }

        Collection collection = collectionService.getCollectionByName(data);
        if (collection != null) {
            showCollection(collection, myUser);
        }

    }

    private void showCollection(Collection collection, BotUser botUser) {
        StringBuilder stringBuilder = new StringBuilder("Collection : " + collection.getName());
        List<Question> questionsByCollectionId = questionService.getQuestionsByCollectionId(collection.getId());

        int count = 1;
        for (Question question : questionsByCollectionId) {
            stringBuilder.append("\n").append("Question ").append(count++).append(" : ").append(question.getText());
            List<Answer> variationsByQuestionId = answerService.getVariationsByQuestionId(question.getId());
            int count1 = 1;
            for (Answer variation : variationsByQuestionId) {
                stringBuilder.append("\t").append("Variation ").append(count1++).append(" : ").append(variation.getText());
            }
        }

        sendText(botUser.getChatId(), stringBuilder.toString());
        myUser.setBaseState(BaseState.MAIN_STATE.toString());
    }

    private void showCollections(BotUser botUser, List<Collection> userCollections) {
        SendMessage collections = new SendMessage(botUser.getId(), "Your collections (choose one to see full questions and variations) : ");

        InlineKeyboardButton[][] inlineKeyboardButtons = new InlineKeyboardButton[userCollections.size() + 1][1];

        for (int i = 0; i < userCollections.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton(userCollections.get(i).getName());
            keyboardButton.callbackData(userCollections.get(i).getName());

            inlineKeyboardButtons[i][0] = keyboardButton;
        }
        InlineKeyboardButton button = new InlineKeyboardButton("Back");
        button.callbackData("Back");

        inlineKeyboardButtons[userCollections.size()][0] = button;

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineKeyboardButtons);

        collections.replyMarkup(inlineKeyboardMarkup);

        bot.execute(collections);
    }
}
