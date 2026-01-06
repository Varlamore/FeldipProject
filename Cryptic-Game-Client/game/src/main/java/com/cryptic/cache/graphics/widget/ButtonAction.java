package com.cryptic.cache.graphics.widget;

import java.util.HashMap;

/**
 * @Author https://github.com/ophionb
 * @author Ophion
 */
public class ButtonAction {
    /**
     * Represents a map between a buttonID and runnable functionality.
     */
    private static HashMap<Integer, Runnable> actions = new HashMap<>();

    public static void addAction(int button, Runnable action) {
        actions.put(button, action);
    }

    public static void doAction(int button) {
        actions.getOrDefault(button, () -> {
        }).run();
    }
}
