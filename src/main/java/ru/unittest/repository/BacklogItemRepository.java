package ru.unittest.repository;

import ru.unittest.model.*;

public interface BacklogItemRepository {
    void save(BacklogItem item);
    void delete(int id);
    BacklogItem get(int id);
}
