package ru.unittest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ScrumServiceImpl.class);

    @Override
    public void finishBacklogItem(int itemId, Resolution resolution) {
        final BacklogItem item = backlogItemRepository.get(itemId);
        final Sprint activeSprint = sprintRepository.getActive();

        mayBacklogItemBeFinished(item, activeSprint);

        item.status = ItemStatus.CLOSED;
        item.resolution = Optional.of(resolution);
        backlogItemRepository.save(item);

        activeSprint.completedPoints += item.storyPoints;
        sprintRepository.save(activeSprint);

        log.info("Вероятность закончить спринт теперь " + activeSprint.probabilityOfSuccess());
    }

    static void mayBacklogItemBeFinished(BacklogItem item, Sprint activeSprint) {
        if (item.status != ItemStatus.INPROGRESS) {
            throw new RuntimeException("Тикет не в работе");
        }

        if (activeSprint == null) {
            throw new RuntimeException("Нет активного спринта");
        }

        if (activeSprint.committedItems.stream().noneMatch(i -> i.id == item.id)) {
            throw new RuntimeException("Тикет не включен в активный спринт");
        }
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
