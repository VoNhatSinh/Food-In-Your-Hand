package com.company.sinh_tan.bus;

import android.content.Context;

import com.company.sinh_tan.dal.FoodTypeDAL;
import com.company.sinh_tan.dto.FoodTypeDTO;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/12/2015.
 */
public class FoodTypeBUS {
    private FoodTypeDAL foodTypeDAL;
    private Context context;
    public FoodTypeBUS(Context context)
    {
        foodTypeDAL = new FoodTypeDAL(context);
    }
    public ArrayList<FoodTypeDTO> getListFoodType()
    {
        return foodTypeDAL.getListFoodType();
    }
}
