package ru.unittest.model;

import ru.unittest.math.BudweiserFormula;
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

        return Math.min(munchausenMethod(), BudweiserFormula.calculate(this));
    }

    private double munchausenMethod() {
        return 1.0 / Math.exp(committedItems.size());
    }
}
