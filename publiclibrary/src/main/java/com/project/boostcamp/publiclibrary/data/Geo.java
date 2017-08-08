package com.project.boostcamp.publiclibrary.data;

import com.google.android.gms.maps.model.LatLng;
import com.project.boostcamp.publiclibrary.domain.GeoDTO;

import java.io.Serializable;

/**
 * Created by Hong Tae Joon on 2017-08-02.
 */

public class Geo implements Serializable{
    private String type;
    private double[] coordinates;

    public Geo() {
    }

    public Geo(String type, double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public Geo(String type, double lng, double lat) {
        this.type = type;
        this.coordinates = new double[] { lng, lat };
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

    public GeoDTO toGeoDTO() {
        GeoDTO dto = new GeoDTO();
        dto.setType(type);
        dto.setCoordinates(coordinates);
        return dto;
    }

    public LatLng toLatLng() {
        return new LatLng(coordinates[1], coordinates[0]);
    }
}
