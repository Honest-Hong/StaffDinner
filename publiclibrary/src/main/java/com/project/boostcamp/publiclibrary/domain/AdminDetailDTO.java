package com.project.boostcamp.publiclibrary.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-03.
 */

public class AdminDetailDTO {
    private String id;
    private String name;
    private String phone;
    private String style;
    private String menu;
    private int cost;
    private GeoDTO geo;
    private String image;
    private int type;
    private ArrayList<ReviewDTO> reviews;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public GeoDTO getGeo() {
        return geo;
    }

    public void setGeo(GeoDTO geo) {
        this.geo = geo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewDTO> reviews) {
        this.reviews = reviews;
    }
}
