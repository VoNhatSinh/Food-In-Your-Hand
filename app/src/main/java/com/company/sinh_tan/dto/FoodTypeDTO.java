package com.company.sinh_tan.dto;

import java.io.Serializable;

/**
 * Created by Sinh on 6/12/2015.
 */
public class FoodTypeDTO implements Serializable{
    private int id;
    private String name;
    private String description;
    public FoodTypeDTO()
    {
        id = -1;
        name = "";
        description = "";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
