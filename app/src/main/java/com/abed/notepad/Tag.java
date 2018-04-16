package com.abed.notepad;

/**
 * Created by Abed on 04/16/2018.
 */

public class Tag {

    private String id;
    private String name;
    private boolean checked;

    public Tag() {}

    public Tag(String id, String name, boolean checked) {
        this.id = id;
        this.name = name;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setId() {
        this.id = id;
    }

    public void setName() {
        this.name = name;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
