package org.example.service.task;

public abstract class TaskValidator {
    public static boolean checkTaskName(String taskName) {
        return taskName != null && !taskName.isBlank() && taskName.length() > 4;
    }

    public static boolean checkTaskDescription(String taskDescription) {
        return taskDescription != null && !taskDescription.isBlank() && taskDescription.length() > 15;
    }

}
