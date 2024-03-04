package example.Comsumer;

import example.common.model.User;
import example.common.service.UserService;

public class ConsumerStart {
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword("123456");
        UserService userService = null;
        userService.getUser(user);

    }
}
