package org.example.ui.userUi;

import org.example.dto.response.TaskResponse;
import org.example.dto.response.UserResponse;
import org.example.model.user.User;
import org.example.model.user.UserRole;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;

import static org.example.Main.taskService;
import static org.example.Main.userService;
import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_NUM;
import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_STR;
import static org.example.ui.userUi.CRUDUI.crudWindow;
import static org.example.ui.userUi.ExcelUtilUI.taskDownload_GetDownloadedTasksWindow;
import static org.example.ui.userUi.TaskAssignUi.assignTaskToAUser;

public class ManagerUi {
    public static void managerWindow(UUID userId) {
        while (true) {
            String choose = chooseOperation();
            switch (choose) {
                case "1" -> {
                    UserResponse userResponse = changeRoleOrRemoveUser(); //✔
                    System.out.println(userResponse.getMessage());
                }
                case "2" -> crudWindow(userId);
                case "3" -> {
                    TaskResponse userResponse = assignTaskToAUser(userId); //✔
                    System.out.println(userResponse.getMessage());
                }
                case "4" -> printAllUsers(); // ✔
                case "5" -> taskDownload_GetDownloadedTasksWindow(userId);
                case "0" -> {
                    return;
                }
            }
        }
    }

    private static UserResponse changeUserRole() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new UserResponse()
                    .setMessage("There are no any users yet")
                    .setStatus(321);
        }

        printAllUsers(users);

        try {
            System.out.println("Choose a user by his numbers");
            int index = SCANNER_NUM.nextInt();
            User user = users.get(index - 1);

            return changeUserRole(user);

        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            return new UserResponse()
                    .setStatus(123)
                    .setMessage("Wrong number:");
        }
    }

    private static UserResponse changeRoleOrRemoveUser() {
        String choice;
        do {
            System.out.println("1. Change user role");
            System.out.println("2. Remove user");
            System.out.println("Enter number 1 or 2");
            choice = SCANNER_STR.nextLine();
        } while (!(choice.equals("1") || choice.equals("2")));

        if (choice.equals("1")) {
            return changeUserRole();
        } else
            return removeUser();
    }

    private static UserResponse removeUser() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new UserResponse()
                    .setMessage("There are no any users yet")
                    .setStatus(979);
        }

        printAllUsers(users);

        int status;
        String message;
        try {
            System.out.println("Choose a user by his number:");
            int index = SCANNER_NUM.nextInt();
            User user = users.get(index - 1);

            if (taskService.getAllAssignedActiveTasksToUserByUserId(user.getId()).size() > 0) {
                status = 678;
                message = user.getName() + " has active tasks so you can not remove this user";
            } else {
                userService.remove(user.getId());
                status = 111;
                message = user.getName() + " has been removed";
            }
        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            status = 897;
            message = "Wrong number";
        }

        return new UserResponse()
                .setStatus(status)
                .setMessage(message);
    }

    private static UserResponse changeUserRole(User user) {
        List<UserRole> allUserRoles = userService.getAllUserRoles();
        for (int i = 0; i < allUserRoles.size(); i++) {
            System.out.printf((i + 1) + ". %s\n", allUserRoles.get(i));
        }
        int status;
        String message;
        try {
            System.out.println("Choose a role by its number:");
            int index = SCANNER_NUM.nextInt();
            UserRole userRole = allUserRoles.get(index - 1);

            if (user.getRole().equals(userRole)) {
                status = 345;
                message = "You can not assign previous user role again";

            } else {
                user.setRole(userRole);
                user.setUpdatedDate(LocalDateTime.now());
                userService.update(user);
                status = 555;
                message = "User role has been changed to " + userRole;
            }
        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            status = 123;
            message = "Wrong number";
        }
        return new UserResponse()
                .setMessage(message)
                .setStatus(status);
    }

    private static void printAllUsers() {
        List<UserRole> allUserRoles = userService.getAllUserRoles();

        for (int i = 0; i < allUserRoles.size(); i++)
            System.out.printf((i + 1) + ". %s\n", allUserRoles.get(i));

        try {
            System.out.println("Choose a role by its number:");
            int index = SCANNER_NUM.nextInt();
            UserRole userRole = allUserRoles.get(index - 1);

            List<User> users = userService.getUsersByRole(userRole);

            if (users.isEmpty())
                System.out.println("There are no any users according to this type");
            else
                printAllUsers(users);

        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            System.out.println("Wrong number:");
        }
    }

    public static void printAllUsers(List<User> users) {
        for (int i = 0; i < users.size(); i++)
            System.out.printf((i + 1) + ". %s,   number of assigned tasks: %d\n",
                    users.get(i),
                    taskService.getAllAssignedActiveTasksToUserByUserId(users.get(i).getId()).size());
    }

    private static String chooseOperation() {
        System.out.println("""
                \n1. Change role of users or remove
                2. CRUD
                3. Assign task to a user
                4. See list of users
                5. Download task or get downloaded tasks
                0. Back
                Choose one:""");
        return SCANNER_STR.nextLine();
    }
}
