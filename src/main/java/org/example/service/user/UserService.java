package org.example.service.user;

import org.example.model.user.User;
import org.example.repository.UserRepository;
import org.example.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface UserService extends BaseService<User>, UserRepository {

    @Override
    default void add(User user) {
        List<User> userList = getUserList();
        userList.add(user);
        writeToFile(userList);
    }

    default UUID isEmailExists(String email) {
        for (User user : getUserList()) if (user.getEmail().equals(email)) return user.getId();
        return null;
    }

    @Override
    default User getById(UUID id) {
        for (User user : getUserList()) if (user.getId().equals(id)) return user;
        return null;
    }
}
