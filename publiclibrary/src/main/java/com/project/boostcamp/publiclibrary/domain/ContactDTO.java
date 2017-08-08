package com.project.boostcamp.publiclibrary.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hong Tae Joon on 2017-08-07.
 */

public class ContactDTO implements Parcelable{
    private String appTitle;
    private int appNumber;
    private long appTime;
    private String appStyle;
    private String appMenu;
    private long appWritedTime;
    private GeoDTO appGeo;
    private String clientName;
    private String clientPhone;
    private String adminName;
    private String adminPhone;
    private GeoDTO adminGeo;
    private String estimateMessage;
    private long estimateTime;
    private long contactTime;

    public ContactDTO() {
    }

    protected ContactDTO(Parcel in) {
        appTitle = in.readString();
        appNumber = in.readInt();
        appTime = in.readLong();
        appStyle = in.readString();
        appMenu = in.readString();
        appWritedTime = in.readLong();
        appGeo = in.readParcelable(GeoDTO.class.getClassLoader());
        clientName = in.readString();
        clientPhone = in.readString();
        adminName = in.readString();
        adminPhone = in.readString();
        adminGeo = in.readParcelable(GeoDTO.class.getClassLoader());
        estimateMessage = in.readString();
        estimateTime = in.readLong();
        contactTime = in.readLong();
    }

    public static final Creator<ContactDTO> CREATOR = new Creator<ContactDTO>() {
        @Override
        public ContactDTO createFromParcel(Parcel in) {
            return new ContactDTO(in);
        }

        @Override
        public ContactDTO[] newArray(int size) {
            return new ContactDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(appTitle);
        parcel.writeInt(appNumber);
        parcel.writeLong(appTime);
        parcel.writeString(appStyle);
        parcel.writeString(appMenu);
        parcel.writeLong(appWritedTime);
        parcel.writeParcelable(appGeo, i);
        parcel.writeString(clientName);
        parcel.writeString(clientPhone);
        parcel.writeString(adminName);
        parcel.writeString(adminPhone);
        parcel.writeParcelable(adminGeo, i);
        parcel.writeString(estimateMessage);
        parcel.writeLong(estimateTime);
        parcel.writeLong(contactTime);
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public int getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(int appNumber) {
        this.appNumber = appNumber;
    }

    public long getAppTime() {
        return appTime;
    }

    public void setAppTime(long appTime) {
        this.appTime = appTime;
    }

    public String getAppStyle() {
        return appStyle;
    }

    public void setAppStyle(String appStyle) {
        this.appStyle = appStyle;
    }

    public String getAppMenu() {
        return appMenu;
    }

    public void setAppMenu(String appMenu) {
        this.appMenu = appMenu;
    }

    public long getAppWritedTime() {
        return appWritedTime;
    }

    public void setAppWritedTime(long appWritedTime) {
        this.appWritedTime = appWritedTime;
    }

    public GeoDTO getAppGeo() {
        return appGeo;
    }

    public void setAppGeo(GeoDTO appGeo) {
        this.appGeo = appGeo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public GeoDTO getAdminGeo() {
        return adminGeo;
    }

    public void setAdminGeo(GeoDTO adminGeo) {
        this.adminGeo = adminGeo;
    }

    public String getEstimateMessage() {
        return estimateMessage;
    }

    public void setEstimateMessage(String estimateMessage) {
        this.estimateMessage = estimateMessage;
    }

    public long getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(long estimateTime) {
        this.estimateTime = estimateTime;
    }

    public long getContactTime() {
        return contactTime;
    }

    public void setContactTime(long contactTime) {
        this.contactTime = contactTime;
    }
}
