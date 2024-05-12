package uz.pdp.backend.service.user_service;

import com.pengrad.telegrambot.model.User;
import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.bot.enums.bot_state.base.BaseState;

import java.util.List;
import java.util.Objects;

public class UserServiceImpl implements UserService {


    private final FileManager<BotUser> fileManager;

    public UserServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/bot_users.txt");

    }

    private static UserService userService;

    public static UserService getInstance() {
        return userService == null ? new UserServiceImpl() : userService;
    }

    @Override
    public BotUser getOrCreate(User user) {
        BotUser botUser = getUserById(user.id());

        if (botUser == null) {
            botUser = BotUser.builder()
                    .chatId(user.id())
                    .firstName(user.firstName())
                    .lastName(user.lastName())
                    .userName(user.username())
                    .baseState(BaseState.MAIN_STATE.toString())
                    .build();

            add(botUser);
        }
        return botUser;
    }

    @Override
    public BotUser getUserById(Long id) {
        List<BotUser> botUsers = fileManager.load(BotUser.class);
        for (BotUser botUser : botUsers) {
            if (Objects.equals(botUser.getChatId(), id)) {
                return botUser;
            }
        }
        return null;
    }

    @Override
    public void update(BotUser botUser) {
        List<BotUser> botUsers = fileManager.load(BotUser.class);
        for (int i = 0; i < botUsers.size(); i++) {
            if (Objects.equals(botUser.getChatId(), botUsers.get(i).getChatId())) {
                botUsers.set(i, botUser);
            }
        }
        fileManager.write(botUsers, BotUser.class);
    }

    @Override
    public void add(BotUser botUser) {
        List<BotUser> botUsers = fileManager.load(BotUser.class);
        botUsers.add(botUser);
        fileManager.write(botUsers, BotUser.class);
    }
}
