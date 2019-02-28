package ru.unittest.math;

import ru.unittest.model.Sprint;

public class BudweiserFormula {
    public static double calculate(Sprint sprint) {
        int k = (int)Math.ceil(sprint.committedItems.size() / 3);
        int totalPoints = sprint.committedItems.stream().map(i -> i.storyPoints).reduce(0, Integer::sum);
        double d = k * (((double)totalPoints) / sprint.completedPoints);
        return 1.0 / d;
    }
}
