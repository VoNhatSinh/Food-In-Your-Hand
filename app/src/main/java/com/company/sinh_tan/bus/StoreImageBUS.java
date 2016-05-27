package com.company.sinh_tan.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.company.sinh_tan.dal.StoreImageDAL;
import com.company.sinh_tan.dto.StoreImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sinh on 6/14/2015.
 */
public class StoreImageBUS {
    private StoreImageDAL storeImageDAL;
    private Context context;
    public StoreImageBUS(Context context)
    {
        this.context = context;
        storeImageDAL = new StoreImageDAL(context);
    }

    public ArrayList<StoreImage> getListStore()
    {
        ArrayList<StoreImage> storeImages = storeImageDAL.getListStore();
        for(int i = 0, size = storeImages.size(); i < size; i++)
        {
            InputStream inImage = null;
            try {
                inImage = context.getAssets().open(storeImages.get(i).getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(inImage);
                storeImages.get(i).setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return storeImages;
    }

    public ArrayList<StoreImage>getListStoreImageByType(ArrayList<String>foodTypes)
    {
        ArrayList<StoreImage> storeImages = storeImageDAL.getListStoreImageByType(foodTypes);
        for(int i = 0, size = storeImages.size(); i < size; i++)
        {
            InputStream inImage = null;
            try {
                inImage = context.getAssets().open(storeImages.get(i).getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(inImage);
                storeImages.get(i).setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return storeImages;
    }

    public ArrayList<StoreImage>getListStoreImageByTypeAndDistrict(ArrayList<String>foodTypes, String idDistrict)
    {
        ArrayList<StoreImage> storeImages = storeImageDAL.getListStoreImageByTypeAndDistrict(foodTypes, idDistrict);
        for(int i = 0, size = storeImages.size(); i < size; i++)
        {
            InputStream inImage = null;
            try {
                inImage = context.getAssets().open(storeImages.get(i).getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(inImage);
                storeImages.get(i).setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return storeImages;
    }
}
