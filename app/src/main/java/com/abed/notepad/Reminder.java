package com.abed.notepad;

/**
 * Created by Abed on 04/22/2018.
 */

public class Reminder {
    private int id;
    private long triggerTime;

    public Reminder() {}

    public Reminder(int id, long triggerTime) {
        this.id = id;
        this.triggerTime = triggerTime;
    }

    public int getId() {
        return id;
    }

    public long getTriggerTime() {
        return triggerTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTriggerTime(long triggerTime) {
        this.triggerTime = triggerTime;
    }
}
