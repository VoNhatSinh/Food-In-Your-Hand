package com.company.sinh_tan.dto;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Tan on 6/13/2015.
 */
public class StoreImage implements Serializable{
    private int id;
    private String name;
    private String address;
    private String image;
    private Bitmap bitmap;


    public StoreImage()
    {
        id = -1;
        name = "";
        address = "";
        image = "";
        bitmap = null;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
