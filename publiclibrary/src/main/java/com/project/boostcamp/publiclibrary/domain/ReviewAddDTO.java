package com.project.boostcamp.publiclibrary.domain;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class ReviewAddDTO {
    private String writerId;
    private String content;
    private float rating;
    private long writedTime;

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
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
