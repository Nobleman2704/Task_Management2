package org.example.service.task;

import org.example.model.task.Task;
import org.example.repository.TaskRepository;
import org.example.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface TaskService extends BaseService<Task>, TaskRepository {
    @Override
    default void add(Task task) {
        List<Task> tasks = getTaskList();
        tasks.add(task);
        writeToFile(tasks);
    }

    @Override
    default Task getById(UUID id) {
        for (Task task : getTaskList()) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }
}
