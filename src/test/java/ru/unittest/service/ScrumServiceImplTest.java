package ru.unittest.service;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import ru.unittest.model.BacklogItem;
import ru.unittest.model.BacklogItem.ItemStatus;
import ru.unittest.model.Release;
import ru.unittest.model.Sprint;
import ru.unittest.repository.BacklogItemRepository;
import ru.unittest.repository.SprintRepository;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ScrumServiceImplTest {

    @Test(expected = RuntimeException.class)
    public void mayBacklogItemBeFinished() {
        final BacklogItem backlogItem = new BacklogItem();
        final Sprint sprint = new Sprint();
        ScrumServiceImpl.mayBacklogItemBeFinished(backlogItem, sprint);
    }

    @Test
    public void mayBacklogItemBeFinishedWhenInProgressAndInSprint() {
        assertThat(testFinishedCheck(25, ItemStatus.INPROGRESS, new int[]{13, 25, 66}), nullValue());
    }

    @Test
    public void mayBacklogItemBeFinishedWhenInProgressButNotInSprint() {
        assertThat(testFinishedCheck(25, ItemStatus.INPROGRESS, new int[]{13, 24, 66}),
                Matchers.hasProperty("message", equalTo("Тикет не включен в активный спринт")));
    }

    @Test
    public void mayBacklogItemBeFinishedWhenAlreadyFinished() {
        assertThat(testFinishedCheck(30, ItemStatus.CLOSED, new int[]{13, 30, 66}),
                Matchers.hasProperty("message", equalTo("Тикет не в работе")));
    }

    private Exception testFinishedCheck(int id, ItemStatus status, int[] sprintItemIds) {
        final BacklogItem backlogItem = new BacklogItem();
        final Sprint sprint = new Sprint();
        backlogItem.id = id;
        backlogItem.status = status;

        for (int sprintItemId : sprintItemIds) {
            sprint.committedItems.add(new BacklogItem() {{
                id = sprintItemId;
            }});
        }

        try {
            ScrumServiceImpl.mayBacklogItemBeFinished(backlogItem, sprint);
            return null;
        } catch (Exception ex) {
            return ex;
        }
    }

    @Test
    public void testWithoutBuilders() {
        SprintRepository sprintRepository = mock(SprintRepository.class);
        BacklogItemRepository backlogItemRepository = mock(BacklogItemRepository.class);

        ScrumServiceImpl service = new ScrumServiceImpl();
        service.setSprintRepository(sprintRepository);
        service.setBacklogItemRepository(backlogItemRepository);


        Release release = service.scheduleFinishedWorkForRelease(makeSprintWithItems(1, 2, 2, 1), "release_001");
    }

    private Sprint makeSprintWithItems(int doneItems, int cancelledItems, int createdItems, int inProgressItems) {
        Sprint sprint = new Sprint();
        for (int i = 0; i < doneItems; i ++) sprint.committedItems.add(new BacklogItem() {{ status = ItemStatus.CLOSED; resolution = Optional.of(Resolution.DONE); }});
        sprint.committedItems.add(new BacklogItem() {{ status = ItemStatus.CLOSED; resolution = Optional.of(Resolution.CANCELLED); }});
        sprint.committedItems.add(new BacklogItem() {{ status = ItemStatus.CREATED; resolution = Optional.empty(); }});
        sprint.committedItems.add(new BacklogItem() {{ status = ItemStatus.INPROGRESS; resolution = Optional.empty(); }});
        return sprint;
    }
}