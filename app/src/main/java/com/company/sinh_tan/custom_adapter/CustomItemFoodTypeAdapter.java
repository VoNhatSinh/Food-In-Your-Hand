package com.company.sinh_tan.custom_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.sinh_tan.dto.FoodTypeDTO;
import com.company.sinh_tan.foodinyourhand.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinh on 6/26/2015.
 */
public class CustomItemFoodTypeAdapter  extends ArrayAdapter<FoodTypeDTO> {
    private Context context;
    private int resource;
    private ArrayList<FoodTypeDTO> objects;
    public CustomItemFoodTypeAdapter(Context context, int resource, ArrayList<FoodTypeDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        TextView txtFoodType = (TextView) convertView.findViewById(R.id.txtFoodType);
        txtFoodType.setText(getItem(position).getName());
        return convertView;
    }
}
