package com.project.boostcamp.publiclibrary.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.project.boostcamp.publiclibrary.data.Geo;

/**
 * Created by Hong Tae Joon on 2017-08-03.
 */

public class GeoDTO implements Parcelable{
    private String type;
    private double[] coordinates;

    public GeoDTO() {
    }

    protected GeoDTO(Parcel in) {
        type = in.readString();
        coordinates = in.createDoubleArray();
    }

    public static final Creator<GeoDTO> CREATOR = new Creator<GeoDTO>() {
        @Override
        public GeoDTO createFromParcel(Parcel in) {
            return new GeoDTO(in);
        }

        @Override
        public GeoDTO[] newArray(int size) {
            return new GeoDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeDoubleArray(coordinates);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public Geo toGeo() {
        Geo g = new Geo();
        g.setCoordinates(coordinates);
        g.setType(type);
        return g;
    }

    public LatLng toLatLng() {
        return new LatLng(coordinates[1], coordinates[0]);
    }
}
