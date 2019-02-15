package ru.unittest.repository;

import ru.unittest.model.*;

import java.util.List;
import java.util.Optional;

public interface SprintRepository {
    List<Sprint> getAll();

    Optional<Sprint> getActive();

    Sprint get(int id);

    void save(Sprint sprint);
}
