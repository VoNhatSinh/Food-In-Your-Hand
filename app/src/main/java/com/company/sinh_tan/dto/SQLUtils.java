package com.company.sinh_tan.dto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Sinh on 6/11/2015.
 */
public class SQLUtils {
    private static SQLiteDatabase mDB;
    private static SQLUtils instance = null;
    private static final Object LockObject = new Object();
    private static final String INT_STORAGE_DATA = "/data/data/";
    private static final String INT_STORAGE_DATABASE = "/databases/";
    private static String databaseName = "DB_FOOD.sqlite";
    private static String urlDatabase = "";
    private static Context context;


    public SQLUtils(Context context)
    {
        this.context = context;

            boolean dbExists = checkExistsDataBase(databaseName);
            String x = urlDatabase;
            mDB = SQLiteDatabase.openOrCreateDatabase(urlDatabase, null);
            if(!dbExists)
            {
                try {
                    copyDatabase(databaseName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("102", "copy database");
            }




    }
    public SQLUtils getInstance(Context context)
    {
        if(instance == null)
        {
            synchronized (LockObject)
            {
                if(instance != null)
                {
                    instance = new SQLUtils(context);
                }
            }
        }
        if(mDB == null || !mDB.isOpen())
        {
            synchronized (LockObject)
            {
                try
                {
                    boolean dbExists = checkExistsDataBase(databaseName);
                    mDB = SQLiteDatabase.openOrCreateDatabase(urlDatabase, null);
                    if(!dbExists)
                    {
                        copyDatabase(databaseName);
                        Log.d("102", "copy database");
                    }




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }

    private  void copyDatabase(String fileName) throws IOException {
        InputStream myInput = context.getAssets().open(fileName);
        String temp = urlDatabase;
        OutputStream myOutput = new FileOutputStream(urlDatabase);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    private  boolean checkExistsDataBase(String fileName) {
        boolean isExistData = false;
        SQLiteDatabase checkDB = null;
        urlDatabase = INT_STORAGE_DATA + context.getPackageName() + INT_STORAGE_DATABASE + fileName;
        int x = 0;

        try {
            checkDB = SQLiteDatabase.openDatabase(urlDatabase, null, SQLiteDatabase.OPEN_READONLY);
            if (checkDB != null) {
                isExistData = true;
            }
            Log.d("101", urlDatabase + " is exist =" + isExistData);

        } catch (SQLiteException e) {
            // database doesn't exist yet.

        } catch (Throwable e) {
        } finally {
            try {
                if (checkDB != null) {
                    checkDB.close();
                    checkDB = null;
                }
            } catch (Exception e) {
            }
        }

        return isExistData;
    }
    public  Cursor rawQuery(String sql, String[]selectionArgs)
    {
        Cursor cursorResult = null;
        boolean isExistDatabase = checkExistsDataBase(databaseName);
        {
            if(isExistDatabase)
            {
                mDB = SQLiteDatabase.openDatabase(urlDatabase, null, SQLiteDatabase.OPEN_READWRITE);
                String name = mDB.getPath();
                if(mDB != null && mDB.isOpen())
                {
                    try
                    {
                        mDB.beginTransaction();
                        cursorResult =  mDB.rawQuery(sql, selectionArgs);
                        mDB.setTransactionSuccessful();
                    }
                    finally {
                        if(mDB != null)
                        {
                            mDB.endTransaction();
                            mDB.close();
                        }
                    }


                }
            }
        }


        return cursorResult;
    }



}
