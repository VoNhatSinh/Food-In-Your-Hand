package com.company.sinh_tan.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.company.sinh_tan.dto.StoreDTO;
import com.company.sinh_tan.lib.StoreLIB;

import java.util.ArrayList;


public class StoreDAL {
    private Context context;
    private  MySqlOpenHelper mySqlOpenHelper;
    public StoreDAL(Context context)
    {

        this.context = context;
        mySqlOpenHelper = MySqlOpenHelper.getInstance(context);
        // mySqlOpenHelper.createDataBase();
    }
    public StoreDTO getStoreById(String id)
    {
        StoreDTO storeDTO = null;
        String[] args = new String[]{id};
        String sql = "SELECT * FROM Store WHERE id = ?";
        Cursor cursor = mySqlOpenHelper.rawQuery(sql, args);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                storeDTO = new StoreDTO();
                storeDTO.setId(cursor.getInt(0));
                storeDTO.setName(cursor.getString(1));
                storeDTO.setKey_name(cursor.getString(2));
                storeDTO.setLongitude(cursor.getString(3));
                storeDTO.setLatitude(cursor.getString(4));
                storeDTO.setPhone(cursor.getString(5));
                storeDTO.setTime(cursor.getString(6));
                storeDTO.setImage(cursor.getString(7));
                storeDTO.setLike(Boolean.valueOf(cursor.getString(8)));
                Log.d("106", "get like " + storeDTO.getLike());
                storeDTO.setId_type(cursor.getInt(9));
                storeDTO.setId_district(cursor.getInt(10));
                storeDTO.setRating(cursor.getFloat(11));
                storeDTO.setAddress(cursor.getString(12));
                storeDTO.setLowest_price(cursor.getDouble(13));
                storeDTO.setHighest_price(cursor.getDouble(14));
                cursor.moveToNext();
            }
            cursor.close();
            mySqlOpenHelper.close();
        }

        return storeDTO;
    }

    public int updateStoreById(ContentValues contentValues, String ID)
    {
        int result = 0;
        String[]args = new String[]{ID};
        String whereClause = StoreLIB.ID + "=?";
        Log.d("106", "id = " + ID);
        result = mySqlOpenHelper.update(StoreLIB.TABLE_NAME,contentValues, whereClause, args);
        mySqlOpenHelper.close();
        return  result;
    }

    public  ArrayList<StoreDTO> getListStoreWithHightRating()
    {
        ArrayList<StoreDTO> storeDTOs = new ArrayList<StoreDTO>();
        String sql = "SELECT * FROM Store WHERE rating >= 4";
        Cursor cursor = mySqlOpenHelper.rawQuery(sql, null);
        if(cursor != null && cursor.getCount() > 0)
        {
            StoreDTO storeDTO = null;
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                storeDTO = new StoreDTO();
                storeDTO.setId(cursor.getInt(0));
                storeDTO.setName(cursor.getString(1));
                storeDTO.setKey_name(cursor.getString(2));
                storeDTO.setLongitude(cursor.getString(3));
                storeDTO.setLatitude(cursor.getString(4));
                storeDTO.setPhone(cursor.getString(5));
                storeDTO.setTime(cursor.getString(6));
                storeDTO.setImage(cursor.getString(7));
                storeDTO.setLike(Boolean.valueOf(cursor.getString(8)));
                Log.d("106", "get like " + storeDTO.getLike());
                storeDTO.setId_type(cursor.getInt(9));
                storeDTO.setId_district(cursor.getInt(10));
                storeDTO.setRating(cursor.getFloat(11));
                storeDTO.setAddress(cursor.getString(12));
                storeDTO.setLowest_price(cursor.getDouble(13));
                storeDTO.setHighest_price(cursor.getDouble(14));

                storeDTOs.add(storeDTO);
                cursor.moveToNext();
            }
            cursor.close();
            mySqlOpenHelper.close();
        }
        return storeDTOs;
    }

    public ArrayList<StoreDTO> getListStoreWithLike()
    {
        ArrayList<StoreDTO> storeDTOs = new ArrayList<StoreDTO>();
        String sql = "SELECT * FROM Store WHERE like='true'";
        Cursor cursor = mySqlOpenHelper.rawQuery(sql, null);
        if(cursor != null && cursor.getCount() > 0)
        {
            StoreDTO storeDTO = null;
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                storeDTO = new StoreDTO();
                storeDTO.setId(cursor.getInt(0));
                storeDTO.setName(cursor.getString(1));
                storeDTO.setKey_name(cursor.getString(2));
                storeDTO.setLongitude(cursor.getString(3));
                storeDTO.setLatitude(cursor.getString(4));
                storeDTO.setPhone(cursor.getString(5));
                storeDTO.setTime(cursor.getString(6));
                storeDTO.setImage(cursor.getString(7));
                storeDTO.setLike(Boolean.valueOf(cursor.getString(8)));
                Log.d("106", "get like " + storeDTO.getLike());
                storeDTO.setId_type(cursor.getInt(9));
                storeDTO.setId_district(cursor.getInt(10));
                storeDTO.setRating(cursor.getFloat(11));
                storeDTO.setAddress(cursor.getString(12));
                storeDTO.setLowest_price(cursor.getDouble(13));
                storeDTO.setHighest_price(cursor.getDouble(14));

                storeDTOs.add(storeDTO);
                cursor.moveToNext();
            }
            cursor.close();
            mySqlOpenHelper.close();
        }
        return storeDTOs;
    }

}
