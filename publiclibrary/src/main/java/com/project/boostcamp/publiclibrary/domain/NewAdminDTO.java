package com.project.boostcamp.publiclibrary.domain;

import com.project.boostcamp.publiclibrary.data.ViewHolderData;

/**
 * Created by Hong Tae Joon on 2017-08-12.
 */

public class NewAdminDTO extends ViewHolderData {
    private String id;
    private String name;
    private int type;
    private String style;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
