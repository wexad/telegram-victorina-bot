package uz.pdp.backend.service.game_service;

import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.backend.model.game.Game;
import uz.pdp.bot.enums.GameStatus;

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
                games.set(i, game);
            }
        }
        fileManager.write(games, Game.class);
    }

    @Override
    public void add(Game game) {
        List<Game> games = fileManager.load(Game.class);
        games.add(game);
        fileManager.write(games, Game.class);
    }

    @Override
    public Game getGameWithNullTime() {
        List<Game> games = fileManager.load(Game.class);
        for (Game game : games) {
            if (Objects.isNull(game.getTimeForQuiz())) {
                return game;
            }
        }
        return null;
    }

    @Override
    public Game getGameOfCurrent(Long chatId) {
        System.out.println("ali");
        List<Game> games = fileManager.load(Game.class);
        for (Game game : games) {
            System.out.println(game.getStatus() + "  " + game.getGroupId() + "  " + chatId);
            if (!game.getStatus().equals(GameStatus.ACTIVE) && Objects.equals(game.getGroupId(), chatId)) {
                return game;
            }
        }
        return null;
    }

    @Override
    public boolean hasGame(Long chatId) {
        List<Game> games = fileManager.load(Game.class);

        for (Game game : games) {
            if (game.getStatus().equals(GameStatus.ACTIVE) && game.getGroupId().equals(chatId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Game getWithoutGroupId() {
        List<Game> games = fileManager.load(Game.class);
        for (Game game : games) {
            if (!game.getStatus().equals(GameStatus.ACTIVE)
                && Objects.isNull(game.getCollectionId())
                && Objects.isNull(game.getTimeForQuiz())) {
                return game;
            }
        }
        return null;
    }
}
