package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.model.question.Question;
import uz.pdp.backend.model.answer.Answer;
import uz.pdp.backend.service.collection_service.CollectionService;
import uz.pdp.backend.service.collection_service.CollectionServiceImpl;
import uz.pdp.backend.service.question_service.QuestionService;
import uz.pdp.backend.service.question_service.QuestionServiceImpl;
import uz.pdp.backend.service.user_service.UserService;
import uz.pdp.backend.service.user_service.UserServiceImpl;
import uz.pdp.backend.service.answer_service.AnswerService;
import uz.pdp.backend.service.answer_service.AnswerServiceImpl;
import uz.pdp.bot.enums.bot_state.base.BaseState;
import uz.pdp.bot.enums.bot_state.child.CreateCollectionState;

import java.util.List;
import java.util.Objects;

public class MessageHandler extends BaseHandler {

    private final UserService userService = UserServiceImpl.getInstance();

    private final CollectionService collectionService = CollectionServiceImpl.getInstance();

    private final QuestionService questionService = QuestionServiceImpl.getInstance();

    private final AnswerService answerService = AnswerServiceImpl.getInstance();

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Chat chat = message.chat();

        User from = message.from();
        String text = message.text();

        myUser = getUserOrCreate(from);
        if (Objects.nonNull(text)) {
            System.out.println(myUser.getUserName() + " : " + text);

            if (Objects.equals(text, "/start")) {
                startMessage(chat.id());
            }

            if (isFromBot(message)) {
                System.out.println(myUser.getBaseState());
                switch (myUser.getBaseState()) {
                    case "MAIN_STATE" -> showMainMenu();

                    case "CREATE_COLLECTION" -> {
                        if (Objects.equals(myUser.getSubState(), "ENTER_NAME_OF_COLLECTION")) {
                            createCollection(text);

                            sendText(myUser.getChatId(), "Enter question please : ");
                        } else if (Objects.equals(myUser.getSubState(), "ENTER_QUESTION")) {

                            Collection lastCollectionUser = collectionService.getLastCollectionUser(myUser);

                            if (lastCollectionUser != null) {
                                createQuestion(lastCollectionUser.getId(), text);

                                String mes = """
                                        Please give 4 possible answers to this question in one message (the first line must contain the correct answer) :
                                        For example :
                                        true
                                        false
                                        false
                                        false
                                        """;

                                sendText(myUser.getChatId(), mes);
                            }
                        } else if (Objects.equals(myUser.getSubState(), CreateCollectionState.ENTER_ANSWER.toString())) {
                            Collection lastCollectionUser = collectionService.getLastCollectionUser(myUser);

                            String[] answers = text.split("\n");

                            if (answers.length >= 2) {
                                createAnswers(answers, lastCollectionUser);

                                createOrAnother(chat);
                            } else {
                                sendText(myUser.getChatId(), "Please send two or more answers to this question! ");
                                sendText(myUser.getChatId(), """
                                        Please give 4 possible answers to this question in one message (the first line must contain the correct answer) :
                                        For example :
                                        true
                                        false
                                        false
                                        false
                                        """);
                            }
                        }
                    }

                    case "MY_COLLECTIONS" -> {
                        Collection collection = collectionService.getCollectionByName(text);
                        if (collection != null) {
                            showCollection(collection);
                        }
                    }
                }
            }
        }


    }

    private void showCollection(Collection collection) {
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

        sendText(myUser.getChatId(), stringBuilder.toString());
        myUser.setBaseState(BaseState.MAIN_STATE.toString());
    }

    private void startMessage(Long id) {
        sendText(id, "Hello! " + myUser.getUserName());
        myUser.setBaseState(BaseState.MAIN_STATE.toString());
        userService.update(myUser);
    }

    private void createAnswers(String[] answers, Collection lastCollectionUser) {
        Question nonFilledQuestion = questionService.getNonFilledQuestion(lastCollectionUser);
        answerService.add(new Answer(answers[0], true, nonFilledQuestion.getId()));
        for (int i = 1; i < answers.length; i++) {
            answerService.add(new Answer(answers[i], false, nonFilledQuestion.getId()));
        }
        nonFilledQuestion.setIsFilled(true);
        questionService.update(nonFilledQuestion);
        myUser.setSubState(CreateCollectionState.CREATE_OR_ANOTHER.toString());
        userService.update(myUser);
    }

    private void createQuestion(String id, String text) {
        Question question = new Question(id, text, false);

        questionService.add(question);
        myUser.setSubState(CreateCollectionState.ENTER_ANSWER.toString());
        userService.update(myUser);
    }

    private void createCollection(String text) {
        Collection collection = new Collection(text, myUser.getUserName(), false);

        collectionService.add(collection);

        myUser.setSubState(CreateCollectionState.ENTER_QUESTION.toString());
        userService.update(myUser);
    }

    private void createOrAnother(Chat chat) {
        SendMessage sendMessage = new SendMessage(chat.id(), "Choose : ");

        InlineKeyboardButton button1 = new InlineKeyboardButton("Add question");
        button1.callbackData("ADD_QUESTION");

        InlineKeyboardButton button2 = new InlineKeyboardButton("Create a new collection");
        button2.callbackData("FINISH_COLLECTION");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(button1, button2);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);
    }

    private SendMessage getSendMessage() {
        SendMessage addOrFinish = new SendMessage(myUser.getChatId(), "Choose : ");
        KeyboardButton keyboardButton1 = new KeyboardButton("Add question");

        KeyboardButton keyboardButton2 = new KeyboardButton("Finish creating collection");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton1, keyboardButton2);

        addOrFinish.replyMarkup(replyKeyboardMarkup);
        return addOrFinish;
    }

    public void showMainMenu() {
        SendMessage sendMessage = new SendMessage(myUser.getChatId(), "Menu : ");

        InlineKeyboardButton button1 = new InlineKeyboardButton("My collections");
        button1.callbackData("SHOW_COLLECTIONS");

        InlineKeyboardButton button2 = new InlineKeyboardButton("Create a new collection");
        button2.callbackData("CREATE_COLLECTION");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(button1, button2);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);
    }


    private boolean isFromBot(Message message) {
        Chat chat = message.chat();

        return chat.type().equals(Chat.Type.Private);
    }
}
