package uz.pdp.backend.service.answer_service;

import uz.pdp.backend.model.answer.Answer;
import uz.pdp.backend.service.base_service.BaseService;

import java.util.List;

public interface AnswerService extends BaseService<Answer> {
    List<Answer> getAnswersByQuestionId(String id);

    void add(Answer variation);

    String[] getOptionsByQuestionId(String id);

}
