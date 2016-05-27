package com.company.sinh_tan.dal;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.company.sinh_tan.dto.FoodTypeDTO;

import java.util.ArrayList;


public class FoodTypeDAL {

    private Context context;
    private  MySqlOpenHelper mySqlOpenHelper;
    public FoodTypeDAL(Context context)
    {

        this.context = context;
        mySqlOpenHelper = MySqlOpenHelper.getInstance(context);
        mySqlOpenHelper.createDataBase();
    }


    public ArrayList<FoodTypeDTO> getListFoodType()
    {
        ArrayList<FoodTypeDTO> foodTypeDTOs = new ArrayList<FoodTypeDTO>();
        String sql = "SELECT * FROM Food_Type";
        String[]args = new String[]{};

        Cursor cursor = mySqlOpenHelper.rawQuery(sql, args);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                FoodTypeDTO foodTypeDTO = new FoodTypeDTO();
                foodTypeDTO.setId(cursor.getInt(0));
                foodTypeDTO.setName(cursor.getString(1));
                foodTypeDTO.setDescription(cursor.getString(2));
                foodTypeDTOs.add(foodTypeDTO);
                cursor.moveToNext();
            }
            cursor.close();
            mySqlOpenHelper.close();
        }

        return foodTypeDTOs;
    }

}
