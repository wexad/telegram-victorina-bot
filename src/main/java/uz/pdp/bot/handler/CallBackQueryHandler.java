package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.backend.model.answer.Answer;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.model.game.Game;
import uz.pdp.backend.model.question.Question;
import uz.pdp.bean.BeanController;
import uz.pdp.bot.enums.bot_state.base.BaseState;
import uz.pdp.bot.enums.bot_state.child.CreateCollectionState;
import uz.pdp.bot.enums.bot_state.child.GameState;

import java.util.List;
import java.util.Objects;

public class CallBackQueryHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();

        myUser = getUserOrCreate(callbackQuery.from());

        System.out.println(myUser.getUserName() + " : " + data);

        switch (myUser.getBaseState()) {
            case "MY_COLLECTIONS" -> {
                Collection collectionByName = collectionService.getCollectionByName(data);
                if (collectionByName != null) {
                    showCollection(collectionByName);

                    BeanController.MESSAGE_HANDLER_THREAD_LOCAL.get().showMainMenu();
                }
            }

            case "GAME" -> {
                if (Objects.equals(GameState.CHOOSE_COLLECTION.toString(), myUser.getSubState())) {
                    Collection collection = collectionService.getCollectionByName(data);
                    if (collection != null) {
                        System.out.println("Choose collection");
                        showCollectionForGame(collection);
                        userService.update(myUser);
                        Game game = gameService.getWithoutGroupId();
                        game.setCollectionId(collection.getId());
                        gameService.update(game);
                        sendText(myUser.getChatId(), "Please send time for each quiz in seconds : ");
                        myUser.setBaseState(BaseState.GAME.toString());
                        myUser.setSubState(GameState.GAME_CREATING.toString());
                    }
                }
            }
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
                    BeanController.MESSAGE_HANDLER_THREAD_LOCAL.get().showMainMenu();
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

    private void showCollectionForGame(Collection collection) {
        StringBuilder stringBuilder = new StringBuilder("Collection : " + collection.getName() + "\n");
        List<Question> questionsByCollectionId = questionService.getQuestionsByCollectionId(collection.getId());

        int count = 1;
        for (Question question : questionsByCollectionId) {
            stringBuilder.append("\n").append("Question ").append(count++).append(" : ").append(question.getText());
            List<Answer> variationsByQuestionId = answerService.getAnswersByQuestionId(question.getId());
            int count1 = 1;
            for (Answer variation : variationsByQuestionId) {
                stringBuilder.append("\n").append("Answer ").append(count1++).append(" : ").append(variation.getText());
            }
            stringBuilder.append("\n");
        }

        sendText(myUser.getChatId(), stringBuilder.toString());

    }

    private void showCollection(Collection collection) {
        StringBuilder stringBuilder = new StringBuilder("Collection : " + collection.getName() + "\n");
        List<Question> questionsByCollectionId = questionService.getQuestionsByCollectionId(collection.getId());

        int count = 1;
        for (Question question : questionsByCollectionId) {
            stringBuilder.append("\n").append("Question ").append(count++).append(" : ").append(question.getText());
            List<Answer> variationsByQuestionId = answerService.getAnswersByQuestionId(question.getId());
            int count1 = 1;
            for (Answer variation : variationsByQuestionId) {
                stringBuilder.append("\n").append("Answer ").append(count1++).append(" : ").append(variation.getText());
            }
            stringBuilder.append("\n");
        }

        sendText(myUser.getChatId(), stringBuilder.toString());
        myUser.setBaseState(BaseState.MAIN_STATE.toString());
        userService.update(myUser);
    }

    public void showCollections(List<Collection> userCollections) {
        myUser.setBaseState(BaseState.MY_COLLECTIONS.toString());
        userService.update(myUser);

        SendMessage showCollections = getSendMessage(userCollections);
        SendResponse execute = bot.execute(showCollections);

        if (execute.isOk()) {
            System.out.println("Sent!");
        } else {
            System.out.println("Something wrong! ");
        }
    }

    private SendMessage getSendMessage(List<Collection> userCollections) {
        SendMessage showCollections = new SendMessage(myUser.getChatId(), "Your collections : ");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (Collection userCollection : userCollections) {
            InlineKeyboardButton button = new InlineKeyboardButton(userCollection.getName());
            button.callbackData(userCollection.getName());
            inlineKeyboardMarkup.addRow(button);
        }
        showCollections.replyMarkup(inlineKeyboardMarkup);
        return showCollections;
    }

}
