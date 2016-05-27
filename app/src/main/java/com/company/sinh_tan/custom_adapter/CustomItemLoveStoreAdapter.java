package com.company.sinh_tan.custom_adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.company.sinh_tan.dto.StoreDTO;
import com.company.sinh_tan.foodinyourhand.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sinh on 6/25/2015.
 */
public class CustomItemLoveStoreAdapter extends ArrayAdapter<StoreDTO> {
    private Context context;
    private int resource;
    private ArrayList<StoreDTO> objects;
    public CustomItemLoveStoreAdapter(Context context, int resource, ArrayList<StoreDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_love_store, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddressLoveStore);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtNameLoveStore);
            viewHolder.imgImage = (ImageView) convertView.findViewById(R.id.imgImageLoveStore);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBarLoveStore);
            viewHolder.imgLove = (ImageView) convertView.findViewById(R.id.imgLove);
            viewHolder.position = position;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final StoreDTO storeDTO = getItem(position);
        viewHolder.txtName.setText(storeDTO.getName());
        viewHolder.txtAddress.setText(storeDTO.getAddress());
        Bitmap imageStore = getBitmapFromAssets(storeDTO.getImage());
        if(imageStore != null)
        {
            viewHolder.imgImage.setImageBitmap(imageStore);
        }
        viewHolder.ratingBar.setRating(storeDTO.getRating());
        if(storeDTO.getLike() == true)
        {
            viewHolder.imgLove.setVisibility(View.VISIBLE);
           // Drawable drawable = context.getResources().getDrawable(R.drawable.icon_star_on);
            //viewHolder.imgLove.setImageDrawable(drawable);
        }

        return convertView;
    }

    private Bitmap getBitmapFromAssets(String imageName)
    {
        Bitmap bitmapResult = null;
        InputStream inImage = null;
        try
        {
            inImage = context.getAssets().open(imageName);
            bitmapResult = BitmapFactory.decodeStream(inImage);
            return bitmapResult;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    static class ViewHolder
    {
        ImageView imgImage;
        TextView txtName;
        TextView txtAddress;
        ImageView imgLove;
        RatingBar ratingBar;
        int position;
    }
}
