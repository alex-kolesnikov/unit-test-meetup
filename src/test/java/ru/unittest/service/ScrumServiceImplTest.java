package ru.unittest.service;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import ru.unittest.model.BacklogItem;
import ru.unittest.model.BacklogItem.ItemStatus;
import ru.unittest.model.Sprint;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
}