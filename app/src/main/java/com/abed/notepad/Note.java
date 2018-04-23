package com.abed.notepad;

import java.util.List;

/**
 * Created by Abed on 03/31/2018.
 */

public class Note {
    private String id;
    private String title;
    private String text;
    private String date;
    private List<String> tags;
    private Reminder reminder;

    public Note() {};

    public Note(String id, String title, String text, String date, List<String> tags, Reminder reminder) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.tags = tags;
        this.reminder = reminder;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public List<String> getTags() {
        return tags;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }
}
