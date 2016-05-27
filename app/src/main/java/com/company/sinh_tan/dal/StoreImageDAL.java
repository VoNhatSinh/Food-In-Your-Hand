package com.company.sinh_tan.dal;

import android.content.Context;
import android.database.Cursor;

import com.company.sinh_tan.dto.StoreImage;

import java.util.ArrayList;


public class StoreImageDAL {
    private Context context;
    private MySqlOpenHelper mySqlOpenHelper;
    public StoreImageDAL(Context context)
    {

        this.context = context;
        mySqlOpenHelper = MySqlOpenHelper.getInstance(context);
        mySqlOpenHelper.createDataBase();
    }
    public ArrayList<StoreImage>getListStore()
    {
        ArrayList<StoreImage> storeImageDALs = new ArrayList<StoreImage>();
        String[] args = new String[]{};
        //String sql = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id  AND Food_Type.name LIKE 'Bánh%'";
        String sql = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id";

        Cursor cursor = mySqlOpenHelper.rawQuery(sql, args);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast()== false)
            {
                StoreImage storeImage = new StoreImage();
                storeImage.setId(cursor.getInt(0));
                storeImage.setName(cursor.getString(1));
                storeImage.setAddress(cursor.getString(2));
                storeImage.setImage(cursor.getString(3));
                storeImageDALs.add(storeImage);
                cursor.moveToNext();
            }
            cursor.close();
            mySqlOpenHelper.close();
        }
        return storeImageDALs;
    }
    public ArrayList<StoreImage>getListStoreImageByType(ArrayList<String>foodTypes)
    {
        ArrayList<StoreImage> storeImageDALs = new ArrayList<StoreImage>();
        String[] args = new String[]{};

        String sql = createSqlFromListFoodType(foodTypes);
       // String temp = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id WHERE Food_Type.id =?";
        Cursor cursor = mySqlOpenHelper.rawQuery(sql, args);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast()== false)
            {
                StoreImage storeImage = new StoreImage();
                storeImage.setId(cursor.getInt(0));
                storeImage.setName(cursor.getString(1));
                storeImage.setAddress(cursor.getString(2));
                storeImage.setImage(cursor.getString(3));
                storeImageDALs.add(storeImage);
                cursor.moveToNext();
            }
            cursor.close();
            mySqlOpenHelper.close();
        }
        return storeImageDALs;
    }

    private String createSqlFromListFoodType(ArrayList<String>foodTypes)
    {
        String sql = "";
        if(foodTypes.contains("0"))
        {
            sql = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id";
        }
        else
        {
            StringBuilder builder = new StringBuilder("");
            String sqlTemp = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id WHERE Food_Type.id =" +foodTypes.get(0);
            builder.append(sqlTemp);
            String orSql = " OR Food_Type.id =";
            for(int i = 1; i < foodTypes.size(); i++)
            {
                builder.append(orSql);
                builder.append(foodTypes.get(i));
            }
            sql = builder.toString();
        }

        return  sql;
    }

    public ArrayList<StoreImage>getListStoreImageByTypeAndDistrict(ArrayList<String>foodTypes, String idDistrict)
    {
        ArrayList<StoreImage> storeImageDALs = new ArrayList<StoreImage>();
        String[] args = new String[]{};

        String sql = createSqlFromListFoodType(foodTypes, idDistrict);
        // String temp = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id WHERE Food_Type.id =?";
        Cursor cursor = mySqlOpenHelper.rawQuery(sql, args);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (cursor.isAfterLast()== false)
            {
                StoreImage storeImage = new StoreImage();
                storeImage.setId(cursor.getInt(0));
                storeImage.setName(cursor.getString(1));
                storeImage.setAddress(cursor.getString(2));
                storeImage.setImage(cursor.getString(3));
                storeImageDALs.add(storeImage);
                cursor.moveToNext();
            }
            cursor.close();
            mySqlOpenHelper.close();
        }
        return storeImageDALs;
    }

    private String createSqlFromListFoodType(ArrayList<String>foodTypes, String idDistrict)
    {
        String sql = "";
        if(foodTypes.size() > 0)
        {
            if(foodTypes.contains("0") && idDistrict.equals("0"))
            {
                sql = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store WHERE Store_Menu.id_food_type = Food_Type.id";
            }
            else if(foodTypes.contains("0") && !idDistrict.equals("0"))
            {
                sql = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id" + " WHERE Store.id_district=" + idDistrict;
            }
            else if(!foodTypes.contains("0") && idDistrict.equals("0"))
            {
                StringBuilder builder = new StringBuilder("");
                String sqlTemp = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id WHERE Food_Type.id =" +foodTypes.get(0);
                builder.append(sqlTemp);
                String orSql = " OR Food_Type.id =";
                for(int i = 1; i < foodTypes.size(); i++)
                {
                    builder.append(orSql);
                    builder.append(foodTypes.get(i));
                }
                sql = builder.toString();
            }
            else
            {
                StringBuilder builder = new StringBuilder("");
                String sqlTemp = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id WHERE( Food_Type.id =" +foodTypes.get(0);
                builder.append(sqlTemp);
                String orSql = " OR Food_Type.id =";
                for(int i = 1; i < foodTypes.size(); i++)
                {
                    builder.append(orSql);
                    builder.append(foodTypes.get(i));
                }
                sql = builder.toString() + " )AND Store.id_district="+idDistrict;
            }

        }
        else
        {
            if(idDistrict.equals("0"))
            {
                sql = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store WHERE Store_Menu.id_food_type = Food_Type.id";
            }
            else
            {
                sql = "SELECT Store.id, Store.name, Store.address, Store.image FROM Store INNER JOIN Store_Menu INNER JOIN Food_Type ON Store.id = Store_Menu.id_store AND Store_Menu.id_food_type = Food_Type.id" + " WHERE Store.id_district=" + idDistrict;
            }
        }


        return  sql;
    }

}
