package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPoll;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageHandler extends BaseHandler {

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
                    case "GAME" -> {
                        if (myUser.getSubState().equals(GameState.CHOOSE_COLLECTION.toString())) {
                            System.out.println("Enter to game");
                            Game game = gameService.getGameWithNullTime();
                            int time = 30;
                            if (isNum(text)) {
                                time = Integer.parseInt(text);
                            } else {
                                sendText(myUser.getChatId(), "Entered wrong format. Time was automatically set 15 seconds! ");
                            }
                            game.setTimeForQuiz(time);
                            game.setIsActive(true);
                            gameService.update(game);
                            myUser.setSubState(GameState.QUIZ_TIME.toString());
                            userService.update(myUser);
                            System.out.println("finish");
                            sendText(game.getGroupId(), "Game started! ");
                            sendPolls(game);
                        }
                    }
                }
            } else if (isFromGroup(chat)) {
                myGroup = getGroupOrCreate(message.chat());

                if (myUser.getBaseState().equals("MAIN_STATE")) {
                    if (text.equals("/play")) {
                        if (!gameService.hasGame(myGroup.getChatId())) {
                            List<Collection> userCollections = collectionService.getUserCollections(myUser);

                            if (userCollections.isEmpty()) {
                                sendText(myUser.getChatId(), "You don't have any collection. Please create at least one collection .");
                            } else {
                                showCollectionsForGame(userCollections);
                                gameService.add(new Game(myGroup.getChatId(),null,null,false));
                                myUser.setBaseState(BaseState.GAME.toString());
                                myUser.setSubState(GameState.CHOOSE_COLLECTION.toString());
                                userService.update(myUser);
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendPolls(Game game) {
        Collection collection = collectionService.getCollectionById(game.getCollectionId());
        List<Question> questions = questionService.getQuestionsByCollectionId(collection.getId());
        AtomicInteger current = new AtomicInteger(0);
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        pool.scheduleAtFixedRate(() -> {
            if (questions.size() > current.get()) {
                Question question = questions.get(current.get());
                String[] answers = answerService.getOptionsByQuestionId(question.getId());
                SendPoll poll = new SendPoll(game.getGroupId(), question.getText(), answers);
                poll.type(Poll.Type.quiz);
                poll.correctOptionId(0);
                poll.openPeriod(game.getTimeForQuiz());
                SendResponse response = bot.execute(poll);
                System.out.println(current.get());
                current.incrementAndGet();
                if (response.isOk()) {
                    System.out.println("OK");
                } else {
                    System.out.println("Wrong");
                }
            }
        }, 0, game.getTimeForQuiz(), TimeUnit.SECONDS);
    }

    private void showCollectionsForGame(List<Collection> userCollections) {
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
    private boolean isNum(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFromGroup(Chat chat) {
        Chat.Type type = chat.type();
        return type.equals(Chat.Type.supergroup) || type.equals(Chat.Type.group);
    }

    private boolean isFromGroup(Message message){
        return message.chat().type().equals(Chat.Type.supergroup) || message.chat().type().equals(Chat.Type.group);
    }
    private boolean isFromBot(Message message) {
        return message.chat().type().equals(Chat.Type.Private);
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

}
