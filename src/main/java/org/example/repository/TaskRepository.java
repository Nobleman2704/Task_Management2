package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.model.task.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.example.util.FileUtils.objectMapper;
import static org.example.util.FileUtils.tasksPath;

public interface TaskRepository {
    File file = new File(tasksPath);

    default List<Task> getTaskList() {
        try {
            return objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeToFile(List<Task> taskList) {
        try {
            objectMapper.writeValue(file, taskList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
