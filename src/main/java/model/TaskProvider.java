package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a centralized list of all available tasks in the game.
 */
public class TaskProvider {

    private static final List<Task> TASKS = new ArrayList<>();

    static {
        TASKS.add(new Task("Design a Logo", 300, 2000));
        TASKS.add(new Task("Write an Article", 150, 1000));
        TASKS.add(new Task("Translate Document", 200, 1500));
        TASKS.add(new Task("Create Website Mockup", 400, 3000));
    }

    public static List<Task> getAvailableTasks() {
        return Collections.unmodifiableList(TASKS);
    }
}