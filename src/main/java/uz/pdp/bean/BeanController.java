package uz.pdp.bean;

import uz.pdp.backend.service.answer_service.AnswerServiceImpl;
import uz.pdp.backend.service.collection_service.CollectionServiceImpl;
import uz.pdp.backend.service.game_service.GameServiceImpl;
import uz.pdp.backend.service.group_service.GroupServiceImpl;
import uz.pdp.backend.service.poll_back_service.PollBackService;
import uz.pdp.backend.service.poll_back_service.PollBackServiceImpl;
import uz.pdp.backend.service.question_service.QuestionServiceImpl;
import uz.pdp.backend.service.result_service.ResultServiceImpl;
import uz.pdp.backend.service.user_service.UserServiceImpl;
import uz.pdp.bot.handler.CallBackQueryHandler;
import uz.pdp.bot.handler.MessageHandler;
import uz.pdp.bot.handler.PollAnswerHandler;

public interface BeanController {
    ThreadLocal<UserServiceImpl> USER_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(UserServiceImpl::new);
    ThreadLocal<CollectionServiceImpl> COLLECTION_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(CollectionServiceImpl::new);
    ThreadLocal<GameServiceImpl> GAME_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(GameServiceImpl::new);
    ThreadLocal<GroupServiceImpl> GROUP_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(GroupServiceImpl::new);
    ThreadLocal<QuestionServiceImpl> QUESTION_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(QuestionServiceImpl::new);
    ThreadLocal<ResultServiceImpl> RESULT_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(ResultServiceImpl::new);
    ThreadLocal<AnswerServiceImpl> ANSWER_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(AnswerServiceImpl::new);
    ThreadLocal<PollBackService> POLL_BACK_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(PollBackServiceImpl::new);
    ThreadLocal<MessageHandler> MESSAGE_HANDLER_THREAD_LOCAL = ThreadLocal.withInitial(MessageHandler::new);
    ThreadLocal<CallBackQueryHandler> CALL_BACK_QUERY_HANDLER_THREAD_LOCAL = ThreadLocal.withInitial(CallBackQueryHandler::new);
    ThreadLocal<PollAnswerHandler> POLL_ANSWER_HANDLER_THREAD_LOCAL = ThreadLocal.withInitial(PollAnswerHandler::new);
}
