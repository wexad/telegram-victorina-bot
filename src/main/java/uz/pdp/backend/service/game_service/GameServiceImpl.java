package uz.pdp.backend.service.game_service;

import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.backend.model.game.Game;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class GameServiceImpl implements GameService {
    private static GameService gameService;
    private final FileManager<Game> fileManager;
    public GameServiceImpl() {
        this.fileManager = new FileManager<>("src/main/resources/games.txt");
    }

    public static GameService getInstance() {
        return gameService == null ? new GameServiceImpl() : gameService;
    }

    @Override
    public void update(Game game) {
        List<Game> games = fileManager.load(Game.class);
        for (int i = 0; i < games.size(); i++) {
            if (Objects.equals(games.get(i).getId(), game.getId())) {
                games.set(i,game);
                return;
            }
        }
        fileManager.write(games, Game.class);
    }

    @Override
    public void add(Game game) {
        List<Game> games = fileManager.load(Game.class);
        games.add(game);
        fileManager.write(games,Game.class);
    }
}
