package com.project.boostcamp.publiclibrary.domain;

/**
 * Created by Hong Tae Joon on 2017-08-07.
 */

public class ContactAddDTO {
    private String app_id;
    private String estimate_id;
    private long contactTime;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getEstimate_id() {
        return estimate_id;
    }

    public void setEstimate_id(String estimate_id) {
        this.estimate_id = estimate_id;
    }

    public long getContactTime() {
        return contactTime;
    }

    public void setContactTime(long contactTime) {
        this.contactTime = contactTime;
    }
}
