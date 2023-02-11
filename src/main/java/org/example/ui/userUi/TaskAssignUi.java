package org.example.ui.userUi;

import org.example.dto.response.TaskResponse;
import org.example.model.task.Task;
import org.example.model.task.TaskStatus;
import org.example.model.user.User;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;

import static org.example.Main.taskService;
import static org.example.Main.userService;
import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_NUM;

public class TaskAssignUi {
    public static TaskResponse assignTaskToAUser(UUID userId) {
        List<Task> tasks = taskService.getALlNotAssignedTasks(userId);

        if (tasks.isEmpty()) {
            return new TaskResponse("There are no any tasks", 321);
        }

        CRUDUI.printTasks(tasks);

        try {
            System.out.println("Choose a task by its number:");
            int index = SCANNER_NUM.nextInt();
            Task task = tasks.get(index - 1);
            return assignTaskToAUser(task, userId);

        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            return new TaskResponse("Wrong number", 132);
        }
    }

    private static TaskResponse assignTaskToAUser(Task task, UUID userId) {
        List<User> users = userService.getUserListByTaskType(task.getType(), userId);
        int status;
        String message;
        if (users.isEmpty()) {
            return new TaskResponse("There are no any users compatible to this task type", 321);
        }

        ManagerUi.printAllUsers(users);

        try {
            System.out.println("Choose a user by his number:");
            int index = SCANNER_NUM.nextInt();
            User user = users.get(index - 1);

            task.setAssignedUserId(user.getId());
            task.setStatus(TaskStatus.ASSIGNED);
            task.setUpdatedDate(LocalDateTime.now());
            taskService.update(task);

            status = 213;
            message = "Task has successfully been assigned to " + user.getName();

        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            status = 123;
            message = "Wrong number";
        }
        return new TaskResponse(message, status);
    }
}
