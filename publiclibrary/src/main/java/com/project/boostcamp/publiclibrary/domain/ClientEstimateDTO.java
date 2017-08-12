package com.project.boostcamp.publiclibrary.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.project.boostcamp.publiclibrary.data.ViewHolderData;

/**
 * Created by Hong Tae Joon on 2017-08-04.
 */

public class ClientEstimateDTO extends ViewHolderData implements Parcelable{
    @SerializedName("_id")
    private String estimateId;
    @SerializedName("app_id")
    private String appId;
    private String message;
    private long writedTime;
    private int state;
    private AdminDTO admin;

    public ClientEstimateDTO() {
    }

    protected ClientEstimateDTO(Parcel in) {
        estimateId = in.readString();
        appId = in.readString();
        message = in.readString();
        writedTime = in.readLong();
        state = in.readInt();
        admin = in.readParcelable(AdminDTO.class.getClassLoader());
    }

    public static final Creator<ClientEstimateDTO> CREATOR = new Creator<ClientEstimateDTO>() {
        @Override
        public ClientEstimateDTO createFromParcel(Parcel in) {
            return new ClientEstimateDTO(in);
        }

        @Override
        public ClientEstimateDTO[] newArray(int size) {
            return new ClientEstimateDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(estimateId);
        parcel.writeString(appId);
        parcel.writeString(message);
        parcel.writeLong(writedTime);
        parcel.writeInt(state);
        parcel.writeParcelable(admin, PARCELABLE_WRITE_RETURN_VALUE);
    }

    public String getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(String estimateId) {
        this.estimateId = estimateId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public AdminDTO getAdmin() {
        return admin;
    }

    public void setAdmin(AdminDTO admin) {
        this.admin = admin;
    }
}
