package uz.pdp.backend.service.game_service;

import uz.pdp.backend.model.game.Game;

public class GameServiceImpl implements GameService {
    private static GameService gameService;

    public static GameService getInstance() {
        return gameService == null ? new GameServiceImpl() : gameService;
    }

    @Override
    public void update(Game game) {

    }

    @Override
    public void add(Game game) {

    }
}
