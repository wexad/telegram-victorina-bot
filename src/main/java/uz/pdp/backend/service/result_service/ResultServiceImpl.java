package uz.pdp.backend.service.result_service;

public class ResultServiceImpl implements ResultService {
    private static ResultService resultService;

    public static ResultService getInstance() {
        return resultService == null ? new ResultServiceImpl() : resultService;
    }
}
