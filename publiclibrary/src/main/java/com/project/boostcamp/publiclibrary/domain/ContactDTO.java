package com.project.boostcamp.publiclibrary.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.project.boostcamp.publiclibrary.data.ViewHolderData;

/**
 * Created by Hong Tae Joon on 2017-08-07.
 */

public class ContactDTO extends ViewHolderData implements Parcelable{
    private String _id;
    private String appTitle;
    private int appNumber;
    private long appTime;
    private String appStyle;
    private String appMenu;
    private GeoDTO appGeo;
    private String clientId;
    private String clientName;
    private String clientPhone;
    private String adminId;
    private String adminName;
    private String adminPhone;
    private GeoDTO adminGeo;
    private String estimateMessage;
    private long estimateTime;
    private long contactTime;

    public ContactDTO() {
        super(0);
    }


    protected ContactDTO(Parcel in) {
        super(0);
        _id = in.readString();
        appTitle = in.readString();
        appNumber = in.readInt();
        appTime = in.readLong();
        appStyle = in.readString();
        appMenu = in.readString();
        appGeo = in.readParcelable(GeoDTO.class.getClassLoader());
        clientId = in.readString();
        clientName = in.readString();
        clientPhone = in.readString();
        adminId = in.readString();
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
        parcel.writeString(_id);
        parcel.writeString(appTitle);
        parcel.writeInt(appNumber);
        parcel.writeLong(appTime);
        parcel.writeString(appStyle);
        parcel.writeString(appMenu);
        parcel.writeParcelable(appGeo, i);
        parcel.writeString(clientId);
        parcel.writeString(clientName);
        parcel.writeString(clientPhone);
        parcel.writeString(adminId);
        parcel.writeString(adminName);
        parcel.writeString(adminPhone);
        parcel.writeParcelable(adminGeo, i);
        parcel.writeString(estimateMessage);
        parcel.writeLong(estimateTime);
        parcel.writeLong(contactTime);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public GeoDTO getAppGeo() {
        return appGeo;
    }

    public void setAppGeo(GeoDTO appGeo) {
        this.appGeo = appGeo;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
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
