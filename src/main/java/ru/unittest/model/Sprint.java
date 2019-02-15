package ru.unittest.model;

import ru.unittest.model.BacklogItem.ItemStatus;

import java.util.ArrayList;
import java.util.List;

public class Sprint {
    public String name;
    public List<BacklogItem> committedItems = new ArrayList<>();

    public boolean isActive;
    public int completedPoints;

    public double probabilityOfSuccess() {
        if (committedItems.stream().allMatch(item -> item.status == ItemStatus.CLOSED)) {
            return 1.0;
        }

        return Math.min(munchausenMethod(), budweiserFormula());
    }

    private double munchausenMethod() {
        return 1.0 / Math.exp(committedItems.size());
    }

    private double budweiserFormula() {
        int k = (int)Math.ceil(committedItems.size() / 3);
        int totalPoints = committedItems.stream().map(i -> i.storyPoints).reduce(0, Integer::sum);
        double d = k * (((double)totalPoints) / completedPoints);
        return 1.0 / d;
    }
}
