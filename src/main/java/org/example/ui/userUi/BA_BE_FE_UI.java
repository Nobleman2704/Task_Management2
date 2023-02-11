package org.example.ui.userUi;

import org.example.dto.response.TaskResponse;
import org.example.model.task.Task;
import org.example.ui.scannerUtil.ScannerUtil;

import java.util.List;
import java.util.UUID;

import static org.example.Main.taskService;
import static org.example.ui.userUi.CRUDUI.crudWindow;
import static org.example.ui.userUi.TaskAssignUi.assignTaskToAUser;

public class BA_BE_FE_UI {
    public static void BA_BE_FE_Window(UUID userId) {
        while (true) {
            String choice = chooseOperation();
            switch (choice) {
                case "1" -> crudWindow(userId); // ✔
                case "2" -> {
                    TaskResponse response = assignTaskToAUser(userId); // ✔
                    System.out.println(response.getMessage());
                }
                case "3" -> printInfoAboutMyActiveTasks(userId); // ✔
                case "0" -> {
                    return;
                }
            }
        }
    }

    private static void printInfoAboutMyActiveTasks(UUID userId) {
        List<Task> tasks = taskService.getAllUserActiveTasks(userId);
        if (tasks.isEmpty()) {
            System.out.println("There are no any active tasks");
            return;
        }
        tasks.forEach(System.out::println);
    }

    private static String chooseOperation() {
        System.out.println("""
                \n1. CRUD
                2. Assign task to a user
                3. See ongoing tasks and assignments
                0. Back
                Choose one:""");
        return ScannerUtil.SCANNER_STR.nextLine();
    }
}
