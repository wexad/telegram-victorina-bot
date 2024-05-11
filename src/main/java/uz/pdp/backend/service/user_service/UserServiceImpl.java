package uz.pdp.backend.service.user_service;

import com.pengrad.telegrambot.model.User;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.bot.enums.bot_state.child.MainState;

import java.util.List;
import java.util.Objects;

public class UserServiceImpl implements UserService {

    private final List<BotUser> botUsers;

    private final FileManager<BotUser> fileManager;

    public UserServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/bot_users.txt");
        botUsers = fileManager.load();
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
                    .baseState(MainState.MAIN_MENU.name())
                    .build();

            add(botUser);
        }
        return botUser;
    }

    @Override
    public BotUser getUserById(Long id) {
        for (BotUser botUser : botUsers) {
            if (Objects.equals(botUser.getChatId(), id)) {
                return botUser;
            }
        }
        return null;
    }

    @Override
    public void update(BotUser botUser) {
        for (int i = 0; i < botUsers.size(); i++) {
            if (botUsers.get(i).equals(botUser)) {
                botUsers.set(i, botUser);
            }
        }

//        fileManager.write(botUsers);
    }

    @Override
    public void add(BotUser botUser) {
        botUsers.add(botUser);
//        fileManager.write(botUsers);
    }
}
