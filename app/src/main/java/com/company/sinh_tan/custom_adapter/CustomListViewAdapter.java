package com.company.sinh_tan.custom_adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.sinh_tan.dto.StoreDTO;
import com.company.sinh_tan.dto.StoreImage;
import com.company.sinh_tan.foodinyourhand.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Sinh on 6/22/2015.
 */
public class CustomListViewAdapter extends ArrayAdapter<StoreDTO> {
    private Context context;
    private int resource;
    private ArrayList<StoreDTO> objects;
    public CustomListViewAdapter(Context context, int resource, ArrayList<StoreDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.lisview_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.imgImage = (ImageView) convertView.findViewById(R.id.imgImage);
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
        new AsyncTask<ViewHolder, Void, Bitmap>() {
            private ViewHolder v;
            @Override
            protected Bitmap doInBackground(ViewHolder... viewHolders) {
                v = viewHolders[0];
                InputStream inImage = null;
                try
                {
                    inImage = context.getAssets().open(storeDTO.getImage());
                    return BitmapFactory.decodeStream(inImage);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if(v.position == position)
                {
                    v.imgImage.setImageBitmap(bitmap);
                }
            }
        }.execute(viewHolder);
       return convertView;
    }
    static class ViewHolder
    {
        ImageView imgImage;
        TextView txtName;
        TextView txtAddress;
        int position;
    }
}
