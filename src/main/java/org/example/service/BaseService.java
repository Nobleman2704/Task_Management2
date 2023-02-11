package org.example.service;

import java.util.UUID;

public interface BaseService<T> {
    void add(T t);

    T getById(UUID id);

    void remove(UUID id);

    void update(T e);
}
