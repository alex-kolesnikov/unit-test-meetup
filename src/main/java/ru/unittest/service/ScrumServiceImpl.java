package ru.unittest.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.unittest.model.BacklogItem;
import ru.unittest.model.BacklogItem.ItemStatus;
import ru.unittest.model.BacklogItem.Resolution;
import ru.unittest.model.Sprint;
import ru.unittest.repository.BacklogItemRepository;
import ru.unittest.repository.SprintRepository;

import java.util.List;
import java.util.Optional;

public class ScrumServiceImpl implements ScrumService {
    @Autowired
    private BacklogItemRepository backlogItemRepository = null;

    @Autowired
    private SprintRepository sprintRepository = null;

    @Override
    public void finishBacklogItem(int itemId, Resolution resolution) {
        final BacklogItem item = backlogItemRepository.get(itemId);

        if (item.status != ItemStatus.INPROGRESS) {
            throw new RuntimeException("Тикет не в работе");
        }

        final Optional<Sprint> activeSprint = sprintRepository.getActive();
        if (!activeSprint.isPresent()) {
            throw new RuntimeException("Нет активного спринта");
        }

        if (activeSprint.get().committedItems.stream().noneMatch(i -> i.id == itemId)) {
            throw new RuntimeException("Тикет не включен в активный спринт");
        }

        item.status = ItemStatus.CLOSED;
        item.resolution = Optional.of(resolution);
        backlogItemRepository.save(item);

        activeSprint.get().completedPoints += item.storyPoints;
        sprintRepository.save(activeSprint.get());
    }

    @Override
    public void deleteBacklogItem(int itemId) {
        final BacklogItem item = backlogItemRepository.get(itemId);

        if (item == null) {
            throw new RuntimeException("Тикет не найден");
        }

        if (item.status != ItemStatus.CREATED) {
            throw new RuntimeException("Нельзя удалять тикет, по которому велась работа");
        }

        final List<Sprint> sprints = sprintRepository.getAll();

        if (sprints.stream().anyMatch(sprint ->
                sprint.committedItems.stream().anyMatch(comittedItem ->
                        comittedItem.id == itemId))) {
            throw new RuntimeException("Тикет включен в спринты");
        }

        backlogItemRepository.delete(itemId);
    }


}
