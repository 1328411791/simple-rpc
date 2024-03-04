package example.provider.servicer;

import example.common.model.User;
import example.common.service.UserService;
import lombok.extern.slf4j.Slf4j;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("UserServiceImpl.getUser: " + user.toString());
        return user;
    }
}
