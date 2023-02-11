package org.example.service.user;

import org.example.dto.response.UserResponse;
import org.example.model.task.TaskType;
import org.example.model.user.User;
import org.example.model.user.UserRole;
import org.example.repository.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class UserServiceImpl implements UserService, UserRepository {
    public List<UserRole> getAllUserRoles() {
        List<UserRole> userRoles = new LinkedList<>();
        userRoles.add(UserRole.BUSINESS_ANALYST);
        userRoles.add(UserRole.FE_LEAD);
        userRoles.add(UserRole.BE_LEAD);
        userRoles.add(UserRole.BACKEND_DEV);
        userRoles.add(UserRole.FRONTEND_DEV);
        userRoles.add(UserRole.QUALITY_ASSURANCE_EN);
        userRoles.add(UserRole.SCRUM_MASTER);
        userRoles.add(UserRole.TESTER);
        userRoles.add(UserRole.USER);
        return userRoles;
    }

    public List<User> getAllUsers() {
        return getUserList()
                .stream()
                .filter(user -> !user.getRole().equals(UserRole.MANAGER))
                .toList();
    }

    public UserResponse signUp(User user) {
        UserResponse userResponse = new UserResponse();
        int status;
        String message;

        String name = user.getName();
        String lastName = user.getLastname();
        String password = user.getPassword();
        String email = user.getEmail();

        if (UserValidator.checkName(name)) {
            status = 154;
            message = "\nName required\n";
        } else if (UserValidator.checkLastName(lastName)) {
            status = 198;
            message = "\nLastname required\n";
        } else if (UserValidator.checkMail(email)) {
            status = 132;
            message = "\nCorrect email format required\n";
        } else if (UserValidator.checkPassword(password)) {
            status = 123;
            message = "\nCorrect password format required\n";
        } else if (isEmailExists(email) != null) {
            status = 128;
            message = "\nEmail \"" + email + "\" exists\n";
        } else {
            status = 777;
            message = "\n" + name + " has successfully been added\n";
            add(user);
        }
        return userResponse
                .setMessage(message)
                .setStatus(status);
    }

    public UserResponse signIn(String email, String password) {
        UserResponse userResponse = new UserResponse();
        int status;
        String message;
        UUID userId = null;

        if (UserValidator.checkMail(email)) {
            status = 132;
            message = "\nCorrect email format required\n";
        } else if (UserValidator.checkPassword(password)) {
            status = 123;
            message = "\nCorrect password format required\n";
        } else {
            userId = isEmailExists(email);
            User user = getById(userId);
            if (userId == null) {
                status = 321;
                message = "\nEmail was not found\n";
            } else if (!user.getPassword().equals(password)) {
                status = 213;
                message = "Email has been found but password did not match";
            } else {
                status = 777;
                message = user.getName() + " has successfully signed in";
            }
        }
        return userResponse
                .setStatus(status)
                .setMessage(message)
                .setUserId(userId);
    }

    @Override
    public void remove(UUID id) {
        writeToFile(getUserList()
                .stream()
                .filter(user -> !user.getId().equals(id))
                .toList());
    }

    public List<User> getUserListByTaskType(TaskType type, UUID userId) {
        User user = getById(userId);

        UserRole role = user.getRole();

        Stream<User> userStream = getUserList().stream();

        if (type.equals(TaskType.BA_TASK))
            userStream = userStream
                    .filter(user1 -> user1.getRole().equals(UserRole.BUSINESS_ANALYST));

        else if (type.equals(TaskType.BE_TASK))
            if (role.equals(UserRole.MANAGER))
                userStream = userStream
                        .filter(user1 -> user1.getRole().equals(UserRole.BACKEND_DEV)
                                || user1.getRole().equals(UserRole.BE_LEAD));
            else
                userStream = userStream
                        .filter(user1 -> user1.getRole().equals(UserRole.BACKEND_DEV));

        else if (type.equals(TaskType.FE_TASK))
            if (role.equals(UserRole.MANAGER))
                userStream = userStream
                        .filter(user1 -> user1.getRole().equals(UserRole.FRONTEND_DEV)
                                || user1.getRole().equals(UserRole.FE_LEAD));
            else
                userStream = userStream
                        .filter(user1 -> user1.getRole().equals(UserRole.FRONTEND_DEV));

        else if (type.equals(TaskType.QA_TASK))
            userStream = userStream
                    .filter(user1 -> user1.getRole().equals(UserRole.QUALITY_ASSURANCE_EN));

        else if (type.equals(TaskType.SM_TASK))
            userStream = userStream
                    .filter(user1 -> user1.getRole().equals(UserRole.SCRUM_MASTER));

        else
            userStream = userStream
                    .filter(user1 -> user1.getRole().equals(UserRole.TESTER));

        return userStream
                .toList();
    }

    public List<User> getUsersByRole(UserRole userRole) {
        return getUserList()
                .stream()
                .filter(user -> user.getRole().equals(userRole))
                .toList();
    }

    @Override
    public void update(User user) {
        UUID userId = user.getId();
        List<User> users = getUserList();
        for (User user1 : users) {
            if (user1.getId().equals(userId)) {
                user1.setName(user.getName())
                        .setLastname(user.getLastname())
                        .setEmail(user.getEmail())
                        .setPassword(user.getPassword())
                        .setRole(user.getRole())
                        .setUpdatedDate(user.getUpdatedDate());
                break;
            }
        }
        writeToFile(users);
    }
}
