package com.project.boostcamp.publiclibrary.domain;

import com.project.boostcamp.publiclibrary.data.ViewHolderData;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class NearAdminDTO extends ViewHolderData {
    private String adminId;
    private int adminType;
    private String name;
    private double distance;
    private String style;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public int getAdminType() {
        return adminType;
    }

    public void setAdminType(int adminType) {
        this.adminType = adminType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
