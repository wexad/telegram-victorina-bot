package uz.pdp.backend.service.poll_back_service;

import uz.pdp.backend.model.poll_back.PollBack;
import uz.pdp.backend.service.base_service.BaseService;

public interface PollBackService extends BaseService<PollBack> {
    PollBack getById(String pollId);
}
