package com.project.boostcamp.publiclibrary.domain;

/**
 * Created by Hong Tae Joon on 2017-08-11.
 */

public class NearAdminDTO {
    private String adminId;
    private int adminType;
    private double distance;

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
