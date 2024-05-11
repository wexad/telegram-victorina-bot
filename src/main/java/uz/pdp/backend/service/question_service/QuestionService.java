package uz.pdp.backend.service.question_service;

import uz.pdp.backend.model.collection.Collection;
import uz.pdp.backend.model.question.Question;
import uz.pdp.backend.service.base_service.BaseService;

import java.util.List;

public interface QuestionService extends BaseService<Question> {
    List<Question> getQuestionsByCollectionId(String id);

    void add(Question question);

    Question getNonFilledQuestionUser(Collection lastCollectionUser);
}
