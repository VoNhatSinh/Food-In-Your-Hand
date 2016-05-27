package com.company.sinh_tan.bus;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.company.sinh_tan.dal.StoreDAL;
import com.company.sinh_tan.dto.StoreDTO;
import com.company.sinh_tan.lib.StoreLIB;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/15/2015.
 */
public class StoreBUS {
    private StoreDAL storeDAL;
    private Context context;
    public StoreBUS(Context context)
    {
        storeDAL = new StoreDAL(context);
    }
    public StoreDTO getStoreById(String id)
    {
        return storeDAL.getStoreById(id);
    }
    public int updateStoreById(StoreDTO storeDTO)
    {
        int result = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(StoreLIB.LIKE, String.valueOf(storeDTO.getLike()));
        contentValues.put(StoreLIB.RATING, storeDTO.getRating());
        Log.d("106", "like " + String.valueOf(storeDTO.getLike()));
        Log.d("106", "rating " + storeDTO.getRating());
        result = storeDAL.updateStoreById(contentValues, String.valueOf(storeDTO.getId()));
        Log.d("106", "result " + result);
        return result;
    }
    public ArrayList<StoreDTO> getListStoreById(ArrayList<String> IDs)
    {
        ArrayList<StoreDTO> result = new ArrayList<StoreDTO>();
        int size = IDs.size();
        if(IDs != null && size > 0)
        {
            for(int i = 0; i < size; i++)
            {
                StoreDTO storeDTO = getStoreById(IDs.get(i));
                if(storeDTO != null)
                {
                    result.add(storeDTO);
                }
            }
        }
        Log.d("108","size list store " + result.size());
        return result;
    }
    public ArrayList<StoreDTO> getListStoreWithLike()
    {
        return  storeDAL.getListStoreWithLike();
    }

    public  ArrayList<StoreDTO> getListStoreWithHightRating()
    {
        return  storeDAL.getListStoreWithHightRating();
    }
}
