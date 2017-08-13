package com.project.boostcamp.publiclibrary.data;

/**
 * Created by Hong Tae Joon on 2017-08-04.
 */

public class ViewHolderData {
    private int viewType;

    public ViewHolderData() {
    }

    public ViewHolderData(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
