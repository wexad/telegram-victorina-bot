package uz.pdp.backend.service.result_service;

import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.backend.model.bot_user.BotUser;
import uz.pdp.backend.model.question.Question;
import uz.pdp.backend.model.result.Result;

import java.util.List;
import java.util.Objects;

public class ResultServiceImpl implements ResultService {
    private static ResultService resultService;
    private final FileManager<Result> fileManager;


    public ResultServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/results.txt");
    }

    public static ResultService getInstance() {
        return resultService == null ? new ResultServiceImpl() : resultService;
    }


    @Override
    public Result getByUsername(String username) {
        List<Result> results = fileManager.load(Result.class);
        for (Result result : results) {
            if (result.getUsername().equals(username)) {
                return result;
            }
        }
        fileManager.write(results, Result.class);
        return null;
    }

    @Override
    public List<Result> getResultsByGroupId(Long chatId) {
        List<Result> results = fileManager.load(Result.class);
        return results.stream()
                .filter(result -> result.getGroupId().equals(chatId))
                .toList();
    }

    @Override
    public void update(Result result) {
        List<Result> results = fileManager.load(Result.class);
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getId().equals(result.getId())) {
                results.set(i, result);
            }
        }
        fileManager.write(results, Result.class);
    }

    @Override
    public void add(Result result) {
        List<Result> results = fileManager.load(Result.class);
        results.add(result);
        fileManager.write(results, Result.class);
    }
}
