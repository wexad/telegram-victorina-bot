package uz.pdp.backend.service.question_service;

import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.model.question.Question;
import uz.pdp.backend.file_manager.FileManager;

import java.util.ArrayList;
import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private static QuestionService questionService;

    public static QuestionService getInstance() {
        return questionService == null ? new QuestionServiceImpl() : questionService;
    }

    private final FileManager<Question> fileManager;


    public QuestionServiceImpl() {
        fileManager = new FileManager<>("src/main/resources/questions.txt");
    }

    @Override
    public List<Question> getQuestionsByCollectionId(String id) {
        List<Question> questions = fileManager.load(Question.class);
        List<Question> questionList = new ArrayList<>();

        for (Question question : questions) {
            if (question.getCollectionId().equals(id)) {
                questionList.add(question);
            }
        }
        return questionList;
    }

    @Override
    public void add(Question question) {
        List<Question> questions = fileManager.load(Question.class);
        questions.add(question);
    }

    @Override
    public Question getNonFilledQuestionUser(Collection lastCollectionUser) {
        List<Question> questions = fileManager.load(Question.class);
        for (Question question : questions) {
            if (question.getCollectionId().equals(lastCollectionUser.getId()) && !question.getIsFilled()) {
                return question;
            }
        }
        return null;
    }
}
