package uz.pdp.backend.service.game_service;

import uz.pdp.backend.model.game.Game;
import uz.pdp.backend.service.base_service.BaseService;

public interface GameService extends BaseService<Game> {
    Game getGameWithNullTime();

    Game getGameOfCurrent(Long chatId);

    boolean hasGame(Long chatId);

    Game getWithoutGroupId();
}
