package uz.pdp.backend.service.group_service;

import uz.pdp.backend.file_manager.FileManager;
import uz.pdp.backend.model.group.Group;
import uz.pdp.backend.service.game_service.GameService;

import java.util.List;
import java.util.Objects;

public class GroupServiceImpl implements GroupService {

    private static GroupService groupService;
    private final FileManager<Group> fileManager;
    public GroupServiceImpl() {
        this.fileManager = new FileManager<>("src/main/resources/groups.txt");
    }
    public static GroupService getInstance() {
        return groupService == null ? new GroupServiceImpl() : groupService;
    }

    @Override
    public void update(Group group) {
        List<Group> groups = fileManager.load(Group.class);
        for (int i = 0; i < groups.size(); i++) {
            if (Objects.equals(group, groups.get(i).getId())) {
                groups.set(i, group);
                return;
            }
        }
        fileManager.write(groups, Group.class);
    }

    @Override
    public void add(Group group) {
        List<Group> groups = fileManager.load(Group.class);
        groups.add(group);
        fileManager.write(groups, Group.class);
    }

    @Override
    public Group getById(Long id) {
        List<Group> groups = fileManager.load(Group.class);
        for (Group group : groups) {
            if (Objects.equals(group.getId(), id)) {
                return group;
            }
        }
        return null;
    }
}
