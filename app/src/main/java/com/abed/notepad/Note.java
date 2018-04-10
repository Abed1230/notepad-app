package com.abed.notepad;

/**
 * Created by Abed on 03/31/2018.
 */

public class Note {
    private String title;
    private String text;
    private String date;
    private String tag;

    public Note() {};

    public Note(String title, String text, String date, String tag) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.tag = tag;
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

    public String getTag() {
        return tag;
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

    public void setTag(String tag) {
        this.tag = tag;
    }
}
