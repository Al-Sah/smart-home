package org.smarthome.climate;

import java.util.Timer;
import java.util.TimerTask;

public class DataImitationTask extends TimerTask {
    private final Timer timer;
    private final Runnable runnable;
    private final ImitationPattern pattern;

    public DataImitationTask(Timer timer, Runnable runnable, ImitationPattern pattern) {
        this.runnable = runnable;
        this.timer = timer;
        this.pattern = pattern;
    }
    @Override
    public void run() {
        runnable.run();
        try {
            timer.schedule(new DataImitationTask(timer, runnable, pattern), pattern.newTime());
        } catch (Exception ignored) {}
    }
}