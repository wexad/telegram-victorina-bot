package uz.pdp.backend.service.question_service;

public class QuestionServiceImpl implements QuestionService {

    private static QuestionService questionService;

    public static QuestionService getInstance() {
        return questionService == null ? new QuestionServiceImpl() : questionService;
    }
}
