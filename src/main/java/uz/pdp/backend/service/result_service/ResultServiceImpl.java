package uz.pdp.backend.service.result_service;

import uz.pdp.backend.file_manager.FileManager;
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
    public void update(Result result) {
        List<Result> results = fileManager.load(Result.class);
        for (int i = 0; i < results.size(); i++) {
            if (Objects.equals(result.getId(),results.get(i))) {
                results.set(i, result);
                return;
            }
        }
        fileManager.write(results,Result.class);
    }

    @Override
    public void add(Result result) {
        List<Result> results = fileManager.load(Result.class);
        results.add(result);
        fileManager.write(results,Result.class);
    }
}
