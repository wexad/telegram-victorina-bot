package uz.pdp.backend.service.user_service;

import com.pengrad.telegrambot.model.User;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.service.base_service.BaseService;

public interface UserService extends BaseService<BotUser> {
    BotUser getOrCreate(User user);

    BotUser getUserById(Long id);

    void update(BotUser botUser);

    void add(BotUser botUser);
}
