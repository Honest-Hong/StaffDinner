package com.project.boostcamp.publiclibrary.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hong Tae Joon on 2017-08-03.
 */

public class AdminDTO implements Parcelable{
    private String id;
    private String name;
    private String phone;
    private String style;
    private String menu;
    private int cost;
    private GeoDTO geo;
    private String image;
    private int type;

    public AdminDTO() {
    }

    protected AdminDTO(Parcel in) {
        id = in.readString();
        name = in.readString();
        phone = in.readString();
        style = in.readString();
        menu = in.readString();
        cost = in.readInt();
        geo = in.readParcelable(GeoDTO.class.getClassLoader());
        image = in.readString();
        type = in.readInt();
    }

    public static final Creator<AdminDTO> CREATOR = new Creator<AdminDTO>() {
        @Override
        public AdminDTO createFromParcel(Parcel in) {
            return new AdminDTO(in);
        }

        @Override
        public AdminDTO[] newArray(int size) {
            return new AdminDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(style);
        parcel.writeString(menu);
        parcel.writeInt(cost);
        parcel.writeParcelable(geo, i);
        parcel.writeString(image);
        parcel.writeInt(type);
    }

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
}
