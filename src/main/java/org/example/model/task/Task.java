package org.example.model.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.model.BaseModel;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Task extends BaseModel {
    private String name;
    private String description;
    private UUID assignedUserId;
    private UUID taskCreatorId;
    private TaskType type;
    private TaskStatus status;

    {
        status = TaskStatus.CREATED;
    }

    public Task(String name, String description, String type, String createdTime, String updatedTime) {
        this.name = name;
        this.description = description;
        this.type = TaskType.valueOf(type);
        createdDate = LocalDateTime.parse(createdTime);
        updatedDate = LocalDateTime.parse(updatedTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, type);
    }

    @Override
    public String toString() {
        return "name=" + name +
                "\n type=" + type +
                "\n status=" + status +
                "\n createdDate=" + createdDate +
                "\n updatedDate=" + updatedDate;
    }
}
