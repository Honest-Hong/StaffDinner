package com.project.boostcamp.publiclibrary.domain;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class EventDTO {
    private int number;
    private String title;
    private String content;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
