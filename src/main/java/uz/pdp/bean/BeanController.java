package uz.pdp.bean;

import uz.pdp.backend.service.collection_service.CollectionServiceImpl;
import uz.pdp.backend.service.game_service.GameServiceImpl;
import uz.pdp.backend.service.group_service.GroupServiceImpl;
import uz.pdp.backend.service.question_service.QuestionServiceImpl;
import uz.pdp.backend.service.result_service.ResultServiceImpl;
import uz.pdp.backend.service.user_service.UserServiceImpl;
import uz.pdp.backend.service.answer_service.AnswerServiceImpl;

public interface BeanController {
    ThreadLocal<UserServiceImpl> USER_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(UserServiceImpl::new);
    ThreadLocal<CollectionServiceImpl> COLLECTION_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(CollectionServiceImpl::new);
    ThreadLocal<GameServiceImpl> GAME_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(GameServiceImpl::new);
    ThreadLocal<GroupServiceImpl> GROUP_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(GroupServiceImpl::new);
    ThreadLocal<QuestionServiceImpl> QUESTION_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(QuestionServiceImpl::new);
    ThreadLocal<ResultServiceImpl> RESULT_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(ResultServiceImpl::new);
    ThreadLocal<AnswerServiceImpl> VARIATION_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(AnswerServiceImpl::new);
}
