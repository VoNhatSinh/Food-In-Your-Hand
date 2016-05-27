package com.company.sinh_tan.custom_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.sinh_tan.foodinyourhand.R;
import com.company.sinh_tan.dto.StoreImage;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/13/2015.
 */
public class GridViewAdapter extends ArrayAdapter<StoreImage> {
    private Context context;
    private int resource;
    private ArrayList<StoreImage> objects;
    public GridViewAdapter(Context context, int resource, ArrayList<StoreImage> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.addressStore = (TextView) row.findViewById(R.id.store_address);
            holder.nameStore = (TextView) row.findViewById(R.id.store_name);
            holder.image = (ImageView)row.findViewById(R.id.imageStore);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }
        StoreImage storeImage = objects.get(position);
        holder.image.setImageBitmap(storeImage.getBitmap());
        holder.addressStore.setText(storeImage.getAddress());
        holder.nameStore.setText(storeImage.getName());
        return row;
    }
    static class ViewHolder {
        TextView nameStore;
        TextView addressStore;
        ImageView image;
    }
}
