package org.example.ui.userUi;

import org.example.dto.response.TaskResponse;
import org.example.model.task.Task;
import org.example.model.task.TaskStatus;
import org.example.model.user.User;
import org.example.ui.scannerUtil.ScannerUtil;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;

import static org.example.Main.taskService;
import static org.example.Main.userService;
import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_NUM;
import static org.example.ui.userUi.ExcelUtilUI.taskDownload_GetDownloadedTasksWindow;

public class OtherUi {
    public static void OtherWindow(UUID userId) {
        while (true) {
            String choice = chooseOperation();
            switch (choice) {
                case "1" -> printMyTasks(userId); // ✔
                case "2" -> {
                    TaskResponse response = changeStatus(userId); // ✔
                    System.out.println(response.getMessage());
                }
                case "3" -> aboutMe(userId);// ✔
                case "4" -> taskDownload_GetDownloadedTasksWindow(userId);
                case "0" -> {
                    return;
                }
            }
        }
    }

    private static void aboutMe(UUID userId) {
        User user = userService.getById(userId);
        System.out.printf("""
                Name: %s
                Lastname: %s
                Email: %s
                Role: %s
                """, user.getName(), user.getLastname(), user.getEmail(), user.getRole());
    }

    private static TaskResponse changeStatus(UUID userId) {
        List<Task> allUserTasks = taskService.getAllUserTasks(userId);

        if (allUserTasks.isEmpty()) {
            return new TaskResponse("You do not have any tasks", 432);
        }

        CRUDUI.printTasks(allUserTasks);

        try {
            System.out.println("Choose a task by its number:");
            int index = SCANNER_NUM.nextInt();
            Task task = allUserTasks.get(index - 1);

            return changeStatus(task);

        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            return new TaskResponse("Wrong button", 324);
        }
    }

    private static TaskResponse changeStatus(Task task) {
        List<TaskStatus> allTaskStatuses = taskService.getAllTaskStatuses();

        for (int i = 0; i < allTaskStatuses.size(); i++) {
            System.out.printf((i + 1) + ". %s\n", allTaskStatuses.get(i));
        }

        int status;
        String message;

        try {
            System.out.println("Choose a status by its number:");
            int index = SCANNER_NUM.nextInt();
            TaskStatus taskStatus = allTaskStatuses.get(index - 1);

            if (taskStatus.equals(task.getStatus())) {
                status = 369;
                message = "You cannot assign previous status again";
            } else {
                task.setStatus(taskStatus);
                task.setUpdatedDate(LocalDateTime.now());
                taskService.update(task);
                status = 980;
                message = "Task status has been changed to " + taskStatus;
            }

        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            status = 788;
            message = "Wrong button";
        }
        return new TaskResponse(message, status);
    }

    private static void printMyTasks(UUID userId) {
        List<Task> tasks = taskService.getAllUserTasks(userId);
        if (tasks.isEmpty()) {
            System.out.println("You do not have any tasks");
            return;
        }
        tasks.forEach(System.out::println);
    }

    private static String chooseOperation() {
        System.out.println("""
                \n1. See my tasks
                2. Change status of my task
                3. About me...
                4. Download task or get downloaded tasks
                0. Back
                Choose one:""");
        return ScannerUtil.SCANNER_STR.nextLine();
    }
}
