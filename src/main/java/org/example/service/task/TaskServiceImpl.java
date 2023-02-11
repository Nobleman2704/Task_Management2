package org.example.service.task;

import org.example.dto.response.TaskResponse;
import org.example.model.task.Task;
import org.example.model.task.TaskStatus;
import org.example.model.task.TaskType;
import org.example.model.user.UserRole;
import org.example.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.example.Main.userService;

public class TaskServiceImpl implements TaskService, TaskRepository {
    public List<TaskType> getAllTaskTypes() {
        List<TaskType> taskTypes = new ArrayList<>();
        taskTypes.add(TaskType.BE_TASK);
        taskTypes.add(TaskType.FE_TASK);
        taskTypes.add(TaskType.BA_TASK);
        taskTypes.add(TaskType.SM_TASK);
        taskTypes.add(TaskType.QA_TASK);
        taskTypes.add(TaskType.TEST);
        return taskTypes;
    }

    public List<TaskStatus> getAllTaskStatuses() {
        List<TaskStatus> taskStatuses = new ArrayList<>();
        taskStatuses.add(TaskStatus.IN_PROGRESS);
        taskStatuses.add(TaskStatus.BLOCKED);
        taskStatuses.add(TaskStatus.DONE);
        return taskStatuses;
    }

    @Override
    public void remove(UUID id) {
        writeToFile(getTaskList()
                .stream()
                .filter(task -> !task.getId().equals(id))
                .toList());
    }

    public List<Task> getAllAssignedActiveTasksToUserByUserId(UUID userId) {
        return getTaskList()
                .stream()
                .filter(task -> task.getAssignedUserId() != null && task.getAssignedUserId().equals(userId)
                        && !(task.getStatus().equals(TaskStatus.DONE) || task.getStatus().equals(TaskStatus.BLOCKED)))
                .toList();
    }

    public List<Task> getALlNotAssignedTasks(UUID userId) {
        UserRole role = userService.getById(userId).getRole();

        if (role.equals(UserRole.MANAGER))
            return getTaskList()
                    .stream()
                    .filter(task -> task.getStatus().equals(TaskStatus.CREATED))
                    .toList();
        else
            return getTaskList()
                    .stream()
                    .filter(task -> task.getStatus().equals(TaskStatus.CREATED)
                            && task.getTaskCreatorId().equals(userId))
                    .toList();
    }

    public List<Task> getAllUserTasks(UUID userId) {
        return getTaskList()
                .stream()
                .filter(task -> task.getAssignedUserId() != null && task.getAssignedUserId().equals(userId))
                .toList();
    }

    public List<Task> getAllUserActiveTasks(UUID userId) {

        UserRole role = userService.getById(userId).getRole();

        if (role.equals(UserRole.MANAGER)) {
            return getTaskList()
                    .stream()
                    .filter(task -> !(task.getStatus().equals(TaskStatus.BLOCKED)
                            || task.getStatus().equals(TaskStatus.DONE)))
                    .toList();
        }

        return getTaskList()
                .stream()
                .filter(task -> task.getTaskCreatorId().equals(userId) && !(task.getStatus().equals(TaskStatus.BLOCKED)
                        || task.getStatus().equals(TaskStatus.DONE)))
                .toList();
    }

    public TaskResponse createTask(String taskName, String taskDescription, UUID userId, TaskType taskType) {
        int status;
        String message;

        if (!TaskValidator.checkTaskName(taskName)) {
            status = 678;
            message = "Task name is not valid";
        } else if (!TaskValidator.checkTaskDescription(taskDescription)) {
            status = 650;
            message = "task description is not valid";
        } else {
            Task task = new Task()
                    .setTaskCreatorId(userId).setName(taskName)
                    .setDescription(taskDescription)
                    .setType(taskType);

            add(task);

            status = 111;
            message = "Task has been created";
        }
        return new TaskResponse(message, status);
    }

    public List<Task> getAllTaskCreatorTasks(UUID userId) {
        UserRole userRole = userService.getById(userId).getRole();

        if (userRole.equals(UserRole.MANAGER))
            return getTaskList();
        else
            return getTaskList()
                    .stream()
                    .filter(task -> task.getTaskCreatorId().equals(userId))
                    .toList();
    }

    public TaskResponse updateTask(int i, Task task, String word) {
        int status;
        String message;
        if (i == 1) {
            if (TaskValidator.checkTaskName(word)) {
                task.setName(word);
                task.setUpdatedDate(LocalDateTime.now());
                update(task);
                status = 666;
                message = "Task name has been changed to " + word;
            } else {
                status = 648;
                message = "Task name is not valid";
            }
        } else {
            if (TaskValidator.checkTaskDescription(word)) {
                task.setDescription(word);
                task.setUpdatedDate(LocalDateTime.now());
                update(task);
                status = 555;
                message = "Task description has been changed to " + word;
            } else {
                status = 348;
                message = "Task description is not valid";
            }
        }
        return new TaskResponse(message, status);
    }

    public List<TaskType> getAllTaskTypes(UserRole role) {
        Stream<TaskType> typeStream = getAllTaskTypes().stream();
        if (role.equals(UserRole.BUSINESS_ANALYST))
            typeStream = typeStream
                    .filter(taskType -> !taskType.equals(TaskType.BA_TASK));

        return typeStream
                .toList();
    }

    @Override
    public void update(Task task) {
        UUID taskId = task.getId();
        List<Task> tasks = getTaskList();
        for (Task task1 : tasks) {
            if (task1.getId().equals(taskId)) {
                task1.setName(task.getName())
                        .setDescription(task.getDescription())
                        .setAssignedUserId(task.getAssignedUserId())
                        .setType(task.getType())
                        .setStatus(task.getStatus())
                        .setUpdatedDate(task.getUpdatedDate());
                break;
            }
        }
        writeToFile(tasks);
    }

    public List<Task> getMyCompletedTasks(UUID userId) {
        return getTaskList()
                .stream()
                .filter(task -> task.getStatus().equals(TaskStatus.DONE)
                        && task.getAssignedUserId() != null
                        && task.getAssignedUserId().equals(userId))
                .toList();
    }
}
