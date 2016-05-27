package com.company.sinh_tan.bus;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.company.sinh_tan.dal.HistorySearchDAL;
import com.company.sinh_tan.dto.StoreDTO;
import com.company.sinh_tan.lib.HistorySearchLIB;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/21/2015.
 */
public class HistorySearchBUS {
    private HistorySearchDAL historySearchDAL;
    private Context context;
    public HistorySearchBUS(Context context)
    {
        historySearchDAL = new HistorySearchDAL(context);
    }
    public long insertIdStore(StoreDTO storeDTO)
    {
        long result = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistorySearchLIB.ID_STORE, storeDTO.getId());
        result = historySearchDAL.insertIdStore(contentValues);
        Log.d("107", "insertId " + result);
        return result;
    }
    public ArrayList<String> getListHistoryID()
    {
        return historySearchDAL.getListHistoryID();
    }
}
