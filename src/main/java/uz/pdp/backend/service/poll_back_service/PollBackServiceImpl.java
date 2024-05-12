package uz.pdp.backend.service.poll_back_service;

import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.backend.model.poll_back.PollBack;

import java.util.List;
import java.util.Objects;

public class PollBackServiceImpl implements PollBackService {

    private static PollBackService pollBackService;
    private final FileManager<PollBack> fileManager;

    public PollBackServiceImpl() {
        this.fileManager = new FileManager<>("src/main/resources/poll_back_answers");
    }

    public static PollBackService getInstance() {
        return pollBackService == null ? new PollBackServiceImpl() : pollBackService;
    }

    @Override
    public void update(PollBack pollBack) {
        List<PollBack> pollBacks = fileManager.load(PollBack.class);
        for (int i = 0; i < pollBacks.size(); i++) {
            if (Objects.equals(pollBack.getId(), pollBacks.get(i).getId())) {
                pollBacks.set(i, pollBack);
            }
        }
        fileManager.write(pollBacks, PollBack.class);
    }

    @Override
    public void add(PollBack pollBack) {
        List<PollBack> pollBacks = fileManager.load(PollBack.class);
        pollBacks.add(pollBack);
        fileManager.write(pollBacks, PollBack.class);
    }
}
