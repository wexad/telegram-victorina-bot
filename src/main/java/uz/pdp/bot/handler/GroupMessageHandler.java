package uz.pdp.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.backend.model.collection.Collection;
import uz.pdp.bean.BeanController;
import uz.pdp.bot.enums.bot_state.base.BaseState;
import uz.pdp.bot.enums.bot_state.child.GameState;

import java.util.List;
import java.util.Objects;

public class GroupMessageHandler extends BaseHandler {
    @Override
    public void handle(Update update) {
        System.out.println("Enter to groupHandel");
        Message message = update.message();

        Chat chat = message.chat();

        User from = message.from();

        myGroup = getGroupOrCreate(chat);

        myUser = getUserOrCreate(from);

        String text = message.text();

        if (Objects.nonNull(text)) {
            if (Objects.equals(text, "/start")) {
                sendText(myGroup.getChatId(), "Hello! " + myUser.getUserName() + " and others! ");
            } else if (Objects.equals(text, "/play")) {
                myUser.setBaseState(BaseState.GAME.toString());
                myUser.setSubState(GameState.CHOOSE_COLLECTION.toString());
                userService.update(myUser);

                if (collectionService.haveCollections(myUser.getUserName())) {
                    chooseCollection();
                }
            }

        }


    }

    private void chooseCollection() {
        List<Collection> userCollections = collectionService.getUserCollections(myUser);

        BeanController.CALL_BACK_QUERY_HANDLER_THREAD_LOCAL.get().showCollections(userCollections);
    }
}
