package org.example;

import org.example.service.task.TaskServiceImpl;
import org.example.service.user.UserServiceImpl;
import org.example.ui.MainUi;

public class Main {
    public static UserServiceImpl userService = new UserServiceImpl();
    public static TaskServiceImpl taskService = new TaskServiceImpl();
    public static void main(String[] args) {
        MainUi.main();
    }
}