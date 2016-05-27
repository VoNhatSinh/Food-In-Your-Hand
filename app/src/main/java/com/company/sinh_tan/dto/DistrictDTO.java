package com.company.sinh_tan.dto;

import java.io.Serializable;

/**
 * Created by Sinh on 6/26/2015.
 */
public class DistrictDTO implements Serializable {
    private int id;
    private String name;
    private String longitude;
    private String latitude;

    public DistrictDTO()
    {
        id = -1;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
