package com.project.boostcamp.publiclibrary.data;

/**
 * Created by Hong Tae Joon on 2017-08-21.
 */

public class StyleListData {
    private String name;
    private boolean checked;

    public StyleListData(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
