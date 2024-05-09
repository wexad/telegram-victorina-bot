package uz.pdp.backend.service.group_service;

import uz.pdp.backend.service.game_service.GameService;

public class GroupServiceImpl implements GroupService {

    private static GroupService groupService;

    public static GroupService getInstance() {
        return groupService == null ? new GroupServiceImpl() : groupService;
    }
}
