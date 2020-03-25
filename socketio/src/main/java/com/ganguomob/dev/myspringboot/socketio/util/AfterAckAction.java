package com.ganguomob.dev.myspringboot.socketio.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wenlongsheng
 * @date 2020/3/25
 */
public class AfterAckAction {
    private final List<Runnable> tasks = new LinkedList<>();

    public AfterAckAction add(Runnable task) {
        tasks.add(task);
        return this;
    }

    public AfterAckAction clear() {
        tasks.clear();
        return this;
    }

    public void run() {
        if (!tasks.isEmpty()) {
            tasks.forEach(Runnable::run);
            tasks.clear();
        }
    }
}