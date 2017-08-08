package com.project.boostcamp.publiclibrary.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hong Tae Joon on 2017-07-25.
 */

public class Estimate implements Parcelable{
    public static final int STATE_WATING = 0x0;
    public static final int STATE_CONTACTED = 0x1;
    public static final int STATE_FAILED = 0x2;
    public static final int STATE_CANCELED = 0x3;

    private int id;
    private String applierName;
    private String restName;
    private String restImgUrl;
    private String restStyle;
    private double restLat;
    private double restLng;
    private String restMenu;
    private int restMenuCost;
    private String message;
    private long writedTime;
    private int state;

    public Estimate() {
    }

    protected Estimate(Parcel in) {
        id = in.readInt();
        applierName = in.readString();
        restName = in.readString();
        restImgUrl = in.readString();
        restStyle = in.readString();
        restLat = in.readDouble();
        restLng = in.readDouble();
        restMenu = in.readString();
        restMenuCost = in.readInt();
        message = in.readString();
        writedTime = in.readLong();
        state = in.readInt();
    }

    public static final Creator<Estimate> CREATOR = new Creator<Estimate>() {
        @Override
        public Estimate createFromParcel(Parcel in) {
            return new Estimate(in);
        }

        @Override
        public Estimate[] newArray(int size) {
            return new Estimate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(applierName);
        parcel.writeString(restName);
        parcel.writeString(restImgUrl);
        parcel.writeString(restStyle);
        parcel.writeDouble(restLat);
        parcel.writeDouble(restLng);
        parcel.writeString(restMenu);
        parcel.writeInt(restMenuCost);
        parcel.writeString(message);
        parcel.writeLong(writedTime);
        parcel.writeInt(state);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplierName() {
        return applierName;
    }

    public void setApplierName(String applierName) {
        this.applierName = applierName;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getRestImgUrl() {
        return restImgUrl;
    }

    public void setRestImgUrl(String restImgUrl) {
        this.restImgUrl = restImgUrl;
    }

    public String getRestStyle() {
        return restStyle;
    }

    public void setRestStyle(String restStyle) {
        this.restStyle = restStyle;
    }

    public double getRestLat() {
        return restLat;
    }

    public void setRestLat(double restLat) {
        this.restLat = restLat;
    }

    public double getRestLng() {
        return restLng;
    }

    public void setRestLng(double restLng) {
        this.restLng = restLng;
    }

    public String getRestMenu() {
        return restMenu;
    }

    public void setRestMenu(String restMenu) {
        this.restMenu = restMenu;
    }

    public int getRestMenuCost() {
        return restMenuCost;
    }

    public void setRestMenuCost(int restMenuCost) {
        this.restMenuCost = restMenuCost;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getWritedTime() {
        return writedTime;
    }

    public void setWritedTime(long writedTime) {
        this.writedTime = writedTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
