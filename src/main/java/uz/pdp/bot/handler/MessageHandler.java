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
import org.jetbrains.annotations.NotNull;
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

import java.util.Objects;

public class MessageHandler extends BaseHandler {

    private final UserService userService = UserServiceImpl.getInstance();

    private final CollectionService collectionService = CollectionServiceImpl.getInstance();

    private final QuestionService questionService = QuestionServiceImpl.getInstance();

    private final AnswerService variationService = AnswerServiceImpl.getInstance();

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
                sendText(chat.id(), "Hello! " + myUser.getUserName());
            }

            if (isFromBot(message)) {
                System.out.println(myUser.getBaseState());
                switch (myUser.getBaseState()) {
                    case "MAIN_STATE" -> showMainMenu(chat);

                    case "CREATE_COLLECTION" -> {
                        if (Objects.equals(myUser.getSubState(), "ENTER_NAME_OF_COLLECTION")) {
                            Collection collection = new Collection(text, myUser.getUserName(), false);

                            collectionService.add(collection);

                            myUser.setSubState(CreateCollectionState.ENTER_QUESTION.toString());
                            userService.update(myUser);
                            sendText(myUser.getChatId(), "Enter question please : ");
                        } else if (Objects.equals(myUser.getSubState(), "ENTER_QUESTION")) {

                            Collection lastCollectionUser = collectionService.getLastCollectionUser(myUser);
                            Question question = new Question(lastCollectionUser.getId(), text, false);

                            questionService.add(question);
                            myUser.setSubState(CreateCollectionState.ENTER_ANSWER.toString());
                            userService.update(myUser);
                            String mes = """
                                    Please give 4 possible answers to this question in one message (the first line must contain the correct answer) :
                                    For example :
                                    true
                                    false
                                    false
                                    false
                                    """;

                            sendText(myUser.getChatId(), mes);
                        } else if (Objects.equals(myUser.getSubState(), CreateCollectionState.ENTER_ANSWER.toString())) {
                            Collection lastCollectionUser = collectionService.getLastCollectionUser(myUser);

                            String[] variations = text.split("\n");

                            if (variations.length >= 2) {
                                Question nonFilledQuestion = questionService.getNonFilledQuestion(lastCollectionUser);
                                variationService.add(new Answer(variations[0], true, nonFilledQuestion.getId()));
                                for (int i = 1; i < variations.length; i++) {
                                    variationService.add(new Answer(variations[i], false, nonFilledQuestion.getId()));
                                }
                                nonFilledQuestion.setIsFilled(true);
                                questionService.update(nonFilledQuestion);
                                myUser.setSubState(CreateCollectionState.CREATE_OR_ANOTHER.toString());
                                userService.update(myUser);
                                createOrAnother(chat);
                            } else {
                                sendText(myUser.getChatId(), "Please send two or more answers to this question! ");
                            }
//                        } else if (Objects.equals(myUser.getSubState(), "CREATE_OR_ANOTHER")) {
//
//                            if (text.equals("Add question")) {
//                                myUser.setSubState(CreateCollectionState.ENTER_QUESTION.toString());
//                            } else if (text.equals("Finish creating collection")) {
//                                Collection lastCollectionUser = collectionService.getLastCollectionUser(myUser);
//                                lastCollectionUser.setIsFinished(true);
//                                collectionService.update(lastCollectionUser);
//                                myUser.setBaseState(BaseState.MAIN_STATE.toString());
//                                myUser.setSubState(null);
//                            }
                        }
                    }
                }
            }
        }


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

    private @NotNull SendMessage getSendMessage() {
        SendMessage addOrFinish = new SendMessage(myUser.getChatId(), "Choose : ");
        KeyboardButton keyboardButton1 = new KeyboardButton("Add question");

        KeyboardButton keyboardButton2 = new KeyboardButton("Finish creating collection");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton1, keyboardButton2);

        addOrFinish.replyMarkup(replyKeyboardMarkup);
        return addOrFinish;
    }

    private void showMainMenu(Chat chat) {
        SendMessage sendMessage = new SendMessage(chat.id(), "Menu : ");

        InlineKeyboardButton button1 = new InlineKeyboardButton("My collections");
        button1.callbackData("SHOW_COLLECTIONS");

        InlineKeyboardButton button2 = new InlineKeyboardButton("Create a new collection");
        button2.callbackData("CREATE_COLLECTION");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(button1, button2);

        sendMessage.replyMarkup(inlineKeyboardMarkup);

        bot.execute(sendMessage);
    }


    private boolean isFromBot(Message message) {
        return message.chat().title() == null;
    }
}
