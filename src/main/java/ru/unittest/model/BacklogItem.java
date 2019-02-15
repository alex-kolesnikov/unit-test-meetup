package ru.unittest.model;

import java.util.Optional;

public class BacklogItem {
    public enum ItemType {
        FEATURE,
        BUG
    }

    public enum ItemStatus {
        CREATED,
        INPROGRESS,
        CLOSED
    }

    public enum Resolution {
        DONE,
        CANCELLED
    }

    public int id;
    public String summary;
    public String description;
    public int storyPoints;
    public ItemStatus status;
    public ItemType type;
    public Optional<Resolution> resolution;
}
