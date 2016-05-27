package com.company.sinh_tan.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.company.sinh_tan.common.constant.Constant;
import com.company.sinh_tan.dto.StoreDTO;
import com.company.sinh_tan.lib.HistorySearchLIB;
import com.company.sinh_tan.lib.StoreLIB;

import java.util.ArrayList;


public class HistorySearchDAL {
    private Context context;
    private  MySqlOpenHelper mySqlOpenHelper;
    public HistorySearchDAL (Context context)
    {

        this.context = context;
        mySqlOpenHelper = MySqlOpenHelper.getInstance(context);
        // mySqlOpenHelper.createDataBase();
    }
    public long insertIdStore(ContentValues contentValues)
    {
        long result = 0;
        result = mySqlOpenHelper.insert(HistorySearchLIB.TABLE_NAME, contentValues);
        mySqlOpenHelper.close();
        return result;
    }
    public ArrayList<String> getListHistoryID()
    {
        ArrayList<String>IDs = new ArrayList<String>();
        String[] args = new String[0];
        String sql = "SELECT * FROM " + HistorySearchLIB.TABLE_NAME;
        Cursor cursor = mySqlOpenHelper.rawQuery(sql, args);
        Log.d("108", "cursor history " + cursor.getCount());
        if(cursor!=null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                IDs.add(String.valueOf(cursor.getInt(1)));
                cursor.moveToNext();
            }
            cursor.close();
            mySqlOpenHelper.close();
        }
        Log.d("108", "size history " + IDs.size());

        return IDs;
    }
}
