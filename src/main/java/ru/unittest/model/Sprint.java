package ru.unittest.model;

import java.util.List;

public class Sprint {
    public String name;
    public List<BacklogItem> committedItems;

    public boolean isActive;
    public int completedPoints;
}
