package uz.pdp.backend.service.result_service;

import uz.pdp.backend.model.result.Result;

public class ResultServiceImpl implements ResultService {
    private static ResultService resultService;

    public static ResultService getInstance() {
        return resultService == null ? new ResultServiceImpl() : resultService;
    }

    @Override
    public void update(Result result) {

    }

    @Override
    public void add(Result result) {

    }
}
