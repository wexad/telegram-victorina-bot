package uz.pdp.backend.service.user_service;

public class UserServiceImpl implements UserService {
    private static UserService userService;

    public static UserService getInstance() {
        return userService == null ? new UserServiceImpl() : userService;
    }
}
