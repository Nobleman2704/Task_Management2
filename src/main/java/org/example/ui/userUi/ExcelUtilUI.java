package org.example.ui.userUi;

import org.example.dto.response.ExcelFileResponse;
import org.example.model.task.Task;
import org.example.ui.excelUtil.ExcelUtil;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_STR;


public class ExcelUtilUI {
    static ExcelUtil excelUtil = new ExcelUtil();

    public static void taskDownload_GetDownloadedTasksWindow(UUID userId) {
        while (true) {
            String choice = choose();
            switch (choice) {
                case "1" -> downloadTask(userId);
                case "2" -> getDownloadedTasks(userId);
                case "0" -> {
                    return;
                }
            }
        }
    }

    private static void getDownloadedTasks(UUID userId) {
        Optional<Set<Task>> myTasks = excelUtil.getMyDownloadedTasks(userId);
        if (myTasks.isPresent()) {
            Set<Task> tasks = myTasks.get();
            for (Task task : tasks) {
                System.out.printf("""
                                Name: %s
                                Description: %s
                                Type: %s
                                Created date: %s
                                Updated date: %s
                                *******************************************************
                                """, task.getName(), task.getDescription(), task.getType(),
                        task.getCreatedDate(), task.getUpdatedDate());
            }
        } else {
            System.out.println("You do not have any downloaded tasks yet");
        }
    }

    private static void downloadTask(UUID userId) {
        ExcelFileResponse response = excelUtil.downLoadMyCompletedTasks(userId);
        System.out.println(response.getMassage());
    }

    private static String choose() {
        System.out.println("""
                \n1. Download my tasks
                2. Get My Downloaded tasks
                0. back
                Choose one:""");
        return SCANNER_STR.nextLine();
    }
}
