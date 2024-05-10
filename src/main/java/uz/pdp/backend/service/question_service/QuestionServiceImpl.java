package uz.pdp.backend.service.question_service;

import uz.pdp.backend.model.question.Question;
import uz.pdp.backend.service.file_manager.FileManager;

import java.util.ArrayList;
import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private static QuestionService questionService;

    public static QuestionService getInstance() {
        return questionService == null ? new QuestionServiceImpl() : questionService;
    }

    private final FileManager<Question> fileManager;

    private final List<Question> questions;

    public QuestionServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/questions.txt");
        this.questions = fileManager.load();
    }

    @Override
    public List<Question> getQuestionsByCollectionId(String id) {
        List<Question> questionList = new ArrayList<>();

        for (Question question : questions) {
            if (question.getCollectionId().equals(id)) {
                questionList.add(question);
            }
        }
        return questionList;
    }
}
