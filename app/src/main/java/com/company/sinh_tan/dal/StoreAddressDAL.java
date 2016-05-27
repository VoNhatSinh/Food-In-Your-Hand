package com.company.sinh_tan.dal;

import android.content.Context;
import android.database.Cursor;

import com.company.sinh_tan.dto.StoreAddress;

import java.util.ArrayList;


public class StoreAddressDAL {
    private Context context;
    private MySqlOpenHelper mySqlOpenHelper;
    public StoreAddressDAL(Context context)
    {

        this.context = context;
        mySqlOpenHelper = MySqlOpenHelper.getInstance(context);
    }
    public ArrayList<StoreAddress> getListStoreAddress()
    {
        ArrayList<StoreAddress> storeAddresses = new ArrayList<StoreAddress>();
        String sql = "SELECT id, name, address, longitude, latitude FROM Store";
        Cursor cursor = mySqlOpenHelper.rawQuery(sql, null);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                StoreAddress storeAddress = new StoreAddress();
                storeAddress.setId(cursor.getInt(0));
                storeAddress.setName(cursor.getString(1));
                storeAddress.setAddress(cursor.getString(2));
                storeAddress.setLongitude(cursor.getString(3));
                storeAddress.setLatitude(cursor.getString(4));
                storeAddresses.add(storeAddress);
                cursor.moveToNext();
            }
        }
        return storeAddresses;
    }
}
