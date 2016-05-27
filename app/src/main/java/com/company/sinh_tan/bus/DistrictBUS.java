package com.company.sinh_tan.bus;

import android.content.Context;

import com.company.sinh_tan.dal.DistrictDAL;
import com.company.sinh_tan.dal.StoreDAL;
import com.company.sinh_tan.dto.DistrictDTO;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/26/2015.
 */
public class DistrictBUS {
    private DistrictDAL districtDAL;
    private Context context;
    public DistrictBUS(Context context)
    {
        districtDAL = new DistrictDAL(context);
    }
    public ArrayList<DistrictDTO> getListDistrict()
    {
        return districtDAL.getListDistrict();
    }
}
