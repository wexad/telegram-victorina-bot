package uz.pdp.backend.service.game_service;

public class GameServiceImpl implements GameService {
    private static GameService gameService;

    public static GameService getInstance() {
        return gameService == null ? new GameServiceImpl() : gameService;
    }
}
