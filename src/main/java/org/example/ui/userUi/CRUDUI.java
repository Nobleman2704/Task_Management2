package org.example.ui.userUi;

import org.example.dto.response.TaskResponse;
import org.example.model.task.Task;
import org.example.model.task.TaskStatus;
import org.example.model.task.TaskType;
import org.example.model.user.UserRole;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.Main.taskService;
import static org.example.Main.userService;
import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_NUM;
import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_STR;

public class CRUDUI {
    public static void crudWindow(UUID userId) {
        while (true) {
            String choice = choose();
            switch (choice) {
                case "1" -> {
                    TaskResponse taskResponse = createTask(userId);
                    System.out.println(taskResponse.getMessage());
                }
                case "2" -> readAllTasks();
                case "3" -> {
                    TaskResponse taskResponse = updateTask(userId);
                    System.out.println(taskResponse.getMessage());
                }
                case "4" -> {
                    TaskResponse taskResponse = deleteTask(userId);
                    System.out.println(taskResponse.getMessage());
                }
                case "0" -> {
                    return;
                }
            }
        }
    }

    private static TaskResponse deleteTask(UUID userId) {
        List<Task> tasks = taskService.getAllTaskCreatorTasks(userId);

        if (tasks.isEmpty()) {
            return new TaskResponse("There are not any tasks", 467);
        }

        printTasks(tasks);

        int status;
        String message;

        try {
            System.out.println("Choose a task by its number:");
            int index = SCANNER_NUM.nextInt();
            Task task = tasks.get(index - 1);

            if (task.getStatus().equals(TaskStatus.IN_PROGRESS)
                    || task.getStatus().equals(TaskStatus.BLOCKED)) {
                status = 798;
                message = "You cannot delete " + task.getStatus() + " project";
            } else {
                taskService.remove(task.getId());
                status = 999;
                message = "task has been removed";
            }

        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            status = 785;
            message = "Wrong button";
        }

        return new TaskResponse(message, status);
    }

    private static TaskResponse updateTask(UUID userId) {
        List<Task> tasks = taskService.getAllUserActiveTasks(userId);

        if (tasks.isEmpty()) {
            return new TaskResponse("There are no any tasks", 645);
        }

        printTasks(tasks);

        try {
            System.out.println("Choose a task by its number:");
            int index = SCANNER_NUM.nextInt();
            Task task = tasks.get(index - 1);

            return updateTask(task);

        } catch (IndexOutOfBoundsException | InputMismatchException e) {
            return new TaskResponse("Wrong message", 988);
        }
    }

    private static TaskResponse updateTask(Task task) {
        String choice;
        do {
            System.out.println("""
                    1. Change task name
                    2. Change task description
                    Choose 1 or 2 to change task
                    """);
            choice = SCANNER_STR.nextLine();
        } while (!(choice.equals("1") || choice.equals("2")));

        if (choice.equals("1")) {
            System.out.println("Write new task name");
            String taskName = SCANNER_STR.nextLine();

            return taskService.updateTask(1, task, taskName);

        } else {
            System.out.println("Write new task description");
            String taskDescription = SCANNER_STR.nextLine();

            return taskService.updateTask(2, task, taskDescription);
        }
    }

    private static void readAllTasks() {
        List<Task> taskList = taskService.getTaskList();
        if (taskList.isEmpty()) {
            System.out.println("There is no any tasks");
            return;
        }
        taskList.forEach(System.out::println);
    }

    private static TaskResponse createTask(UUID userId) {
        System.out.println("Write task name:");
        String taskName = SCANNER_STR.nextLine();
        System.out.println("Write task description:");
        String taskDescription = SCANNER_STR.nextLine();

        Optional<TaskType> optionalTaskType = getTaskType(userId);

        if (optionalTaskType.isEmpty()) {
            return new TaskResponse("You chose wrong number", 869);
        }

        TaskType taskType = optionalTaskType.get();

        return taskService.createTask(taskName, taskDescription, userId, taskType);
    }

    private static Optional<TaskType> getTaskType(UUID userId) {
        UserRole userRole = userService.getById(userId).getRole();

        List<TaskType> allTaskTypes = taskService.getAllTaskTypes(userRole);

        TaskType taskType = null;

        if (userRole.equals(UserRole.MANAGER) || userRole.equals(UserRole.BUSINESS_ANALYST)) {
            for (int i = 0; i < allTaskTypes.size(); i++) {
                System.out.printf((i + 1) + ". %s\n", allTaskTypes.get(i));
            }
            try {
                System.out.println("Choose a task type by its number:");
                int index = SCANNER_NUM.nextInt();
                taskType = allTaskTypes.get(index - 1);

            } catch (IndexOutOfBoundsException | InputMismatchException e) {
                System.out.println("Wrong number:");
            }
        } else if (userRole.equals(UserRole.FE_LEAD))
            taskType = TaskType.FE_TASK;
        else
            taskType = TaskType.BE_TASK;

        return Optional.ofNullable(taskType);
    }

    public static void printTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf((i + 1) + ". %s\n", tasks.get(i));
        }
    }

    private static String choose() {
        System.out.println("""
                \n1. Create task
                2. Read task
                3. Update task
                4. Delete task
                0. Back
                Choose one:""");
        return SCANNER_STR.nextLine();
    }
}
