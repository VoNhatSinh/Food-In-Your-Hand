package com.company.sinh_tan.bus;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.company.sinh_tan.dal.StoreAddressDAL;
import com.company.sinh_tan.dto.StoreAddress;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/17/2015.
 */
public class StoreAddressBUS {
    private StoreAddressDAL storeAddressDAL;
    private Context context;
    public StoreAddressBUS(Context context)
    {
        this.context = context;
        storeAddressDAL = new StoreAddressDAL(context);
    }
    public ArrayList<StoreAddress> getListStoreAddressNearMe(LatLng myLatLng, float distance)
    {
        ArrayList<StoreAddress>storeAddresses = storeAddressDAL.getListStoreAddress();
        ArrayList<StoreAddress> results = new ArrayList<StoreAddress>();
        int size = storeAddresses.size();
        if(size > 0)
        {
            for(int i = 0; i < size; i++)
            {
                if(caculateDistance(myLatLng, storeAddresses.get(i)) <=  distance)
                {
                    results.add(storeAddresses.get(i));
                }
            }
        }
        return results;
    }
    float caculateDistance(LatLng myLatLng, StoreAddress storeAddress)
    {
        float distance = -1;
        float[] arrDistance = new float[1];
        double latitudeStore = Double.valueOf(storeAddress.getLatitude());
        double longtitudeStore = Double.valueOf(storeAddress.getLongitude());
        Location.distanceBetween(myLatLng.latitude, myLatLng.longitude, latitudeStore, longtitudeStore, arrDistance);

        if(arrDistance.length > 0)
        {
            distance = arrDistance[0];
            Log.d("106", "distance store " + distance);
        }
        return distance;
    }
}
