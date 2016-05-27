package com.company.sinh_tan.dto;

import java.io.Serializable;

/**
 * Created by Tan on 6/12/2015.
 */
public class StoreDTO implements Serializable {
    private int id;
    private String name;
    private String key_name;
    private String longitude;
    private String latitude;
    private String phone;
    private String time;
    private String image;
    private boolean like;
    private int id_type;
    private int id_district;
    private float rating;
    private String address;
    private double lowest_price;
    private double highest_price;

    @Override
    public String toString() {
        return this.name;
    }

    public StoreDTO()
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

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isLike() {
        return like;
    }

    public boolean getLike()
    {
        return this.like;
    }
    public void setLike(boolean like) {
        this.like = like;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public int getId_district() {
        return id_district;
    }

    public void setId_district(int id_district) {
        this.id_district = id_district;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLowest_price() {
        return lowest_price;
    }

    public void setLowest_price(double lowest_price) {
        this.lowest_price = lowest_price;
    }

    public double getHighest_price() {
        return highest_price;
    }

    public void setHighest_price(double highest_price) {
        this.highest_price = highest_price;
    }
}
