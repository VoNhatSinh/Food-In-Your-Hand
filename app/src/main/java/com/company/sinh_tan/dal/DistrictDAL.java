package com.company.sinh_tan.dal;

import android.content.Context;
import android.database.Cursor;

import com.company.sinh_tan.dto.DistrictDTO;

import java.util.ArrayList;


public class DistrictDAL {
    private Context context;
    private  MySqlOpenHelper mySqlOpenHelper;
    public DistrictDAL(Context context)
    {
        this.context = context;
        mySqlOpenHelper = MySqlOpenHelper.getInstance(context);
        // mySqlOpenHelper.createDataBase();
    }

    public ArrayList<DistrictDTO> getListDistrict()
    {
        ArrayList<DistrictDTO> districtDTOs = new ArrayList<DistrictDTO>();
        DistrictDTO districtDTO0 = new DistrictDTO();
        districtDTO0.setId(0);
        districtDTO0.setName("Tất cả");
        districtDTO0.setLatitude("");
        districtDTO0.setLongitude("");
        districtDTOs.add(districtDTO0);
        String sql = "SELECT * FROM District";
        Cursor cursor = mySqlOpenHelper.rawQuery(sql, null);
        if(cursor != null && cursor.getCount() >= 0)
        {
            cursor.moveToFirst();
            DistrictDTO districtDTO;
            while (!cursor.isAfterLast())
            {
                districtDTO = new DistrictDTO();
                districtDTO.setId(cursor.getInt(0));
                districtDTO.setName(cursor.getString(1));
                districtDTO.setLongitude(cursor.getString(2));
                districtDTO.setLatitude(cursor.getString(3));
                districtDTOs.add(districtDTO);
                cursor.moveToNext();
            }
        }
        return districtDTOs;
    }

}
