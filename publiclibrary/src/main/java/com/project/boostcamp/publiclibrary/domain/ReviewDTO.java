package com.project.boostcamp.publiclibrary.domain;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class ReviewDTO {
    private String writer;
    private String receiver;
    private String content;
    private float rating;
    private long writedTime;

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getWritedTime() {
        return writedTime;
    }

    public void setWritedTime(long writedTime) {
        this.writedTime = writedTime;
    }
}
