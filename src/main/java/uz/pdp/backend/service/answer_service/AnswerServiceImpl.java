package uz.pdp.backend.service.answer_service;

import kotlin.io.OnErrorAction;
import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.backend.model.answer.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnswerServiceImpl implements AnswerService {

    private static AnswerService answerService;

    public static AnswerService getInstance() {
        return answerService == null ? new AnswerServiceImpl() : answerService;
    }

    private final FileManager<Answer> fileManager;

    public AnswerServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/variations.txt");
    }

    @Override
    public List<Answer> getVariationsByQuestionId(String id) {
        List<Answer> answers = fileManager.load(Answer.class);
        List<Answer> variationList = new ArrayList<>();
        for (Answer variation : answers) {
            if (variation.getQuestionId().equals(id)) {
                variationList.add(variation);
            }
        }
        return variationList;
    }

    @Override
    public void add(Answer answer) {
        List<Answer> answers = fileManager.load(Answer.class);
        answers.add(answer);
        fileManager.write(answers, Answer.class);
    }

    @Override
    public void update(Answer answer) {
        List<Answer> answers = fileManager.load(Answer.class);
        for (int i = 0; i < answers.size(); i++) {
            if (Objects.equals(answer.getId(),answers.get(i).getId())) {
                answers.set(i,answer);
            }
        }
    }
}
