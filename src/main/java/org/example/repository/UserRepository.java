package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.model.user.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.example.util.FileUtils.objectMapper;
import static org.example.util.FileUtils.userPath;

public interface UserRepository {
    File file = new File(userPath);

    default List<User> getUserList() {
        try {
            return objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void writeToFile(List<User> userList) {
        try {
            objectMapper.writeValue(file, userList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
