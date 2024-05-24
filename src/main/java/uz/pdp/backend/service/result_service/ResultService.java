package uz.pdp.backend.service.result_service;

import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.result.Result;
import uz.pdp.backend.service.base_service.BaseService;

import java.util.List;

public interface ResultService extends BaseService<Result> {
    Result getByUsername(String username);

    List<Result> getResultsByGroupId(Long chatId);
}
