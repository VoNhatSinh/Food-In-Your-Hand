package com.company.sinh_tan.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.company.sinh_tan.dto.HistorySearchDTO;
import com.company.sinh_tan.dto.StoreDTO;
import com.company.sinh_tan.lib.HistorySearchLIB;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MySqlOpenHelper extends SQLiteOpenHelper {
    private final String INT_STORAGE_DATA = "/data/data/";
    private final String INT_STORAGE_DATABASE = "/databases/";
    private static String DATABASE_NAME = "DB_FOOD.sqlite";
    private static String urlDatabase = "";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    private SQLiteDatabase database;
    private static MySqlOpenHelper instance;
    private MySqlOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        urlDatabase = INT_STORAGE_DATA + this.context.getPackageName() + INT_STORAGE_DATABASE;

    }

    public static MySqlOpenHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new MySqlOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public synchronized void close() {
        if(database != null)
        {
            database.close();
        }
        super.close();
    }

    public void openDatabase() throws SQLiteException
    {
        String myDatabasePath = urlDatabase + DATABASE_NAME;
        //database = SQLiteDatabase.openDatabase(myDatabasePath, null, SQLiteDatabase.OPEN_READONLY);
        database = this.getReadableDatabase();
        if(database != null)
        {
            Log.d("102", "database is exist");
            database.close();
        }
        else
        {
            Log.d("102", "can not open");
        }

    }
    public void createDataBase()
    {
        boolean isDatabaseExist = checkExistsDataBase(DATABASE_NAME);
        if(!isDatabaseExist)
        {

            this.getReadableDatabase();
         //   database.close();
            try
            {
                    copyDataBase();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private void copyDataBase() throws IOException {
        InputStream myInputStream = context.getAssets().open(DATABASE_NAME);
        String outputFileName = urlDatabase + DATABASE_NAME;
        OutputStream myOutputStream = new FileOutputStream(outputFileName);
        byte[]buffer = new byte[1024];
        int lenght;
        while ((lenght = myInputStream.read(buffer)) > 0)
        {
            myOutputStream.write(buffer, 0, lenght);
        }
        myOutputStream.flush();
        myOutputStream.close();
        myInputStream.close();
    }
    private  boolean checkExistsDataBase(String fileName) {
        boolean isExistData = false;
        SQLiteDatabase checkDB = null;
        String  myDataBasePath = INT_STORAGE_DATA + context.getPackageName() + INT_STORAGE_DATABASE + fileName;


        try {
            checkDB = SQLiteDatabase.openDatabase(myDataBasePath, null, SQLiteDatabase.OPEN_READONLY);;
            if (checkDB != null)
            {
                isExistData = true;
            }
            Log.d("101", urlDatabase + " is exist =" + isExistData);

        }
        catch (SQLiteException e)
        {
            e.printStackTrace(); // database doesn't exist yet.

        }
        if (checkDB != null) {
            checkDB.close();

        }
        return isExistData;
    }

    public int update(String table, ContentValues contentValues, String whereClause, String[]whereArgs)
    {
        int result = 0;
        boolean isExistDatabase = checkExistsDataBase(DATABASE_NAME);
        {
            if(isExistDatabase)
            {
                database = this.getWritableDatabase();
                if(database != null && database.isOpen())
                {
                    try
                    {
                        database.beginTransaction();
                        result = database.update(table, contentValues, whereClause, whereArgs);
                        database.setTransactionSuccessful();
                    }
                    finally {
                        if(database != null)
                        {
                            database.endTransaction();
                            // database.close();
                        }
                    }


                }
            }
        }

        return result;
    }

    public long insert(String table,ContentValues contentValues)
    {
        long result = 0;
        boolean isExistDatabase = checkExistsDataBase(DATABASE_NAME);
        {
            if(isExistDatabase)
            {
                database = this.getWritableDatabase();
                if(database != null && database.isOpen())
                {
                    try
                    {
                        database.beginTransaction();
                        result = database.insert(table, null, contentValues );
                        database.setTransactionSuccessful();
                    }
                    finally {
                        if(database != null)
                        {
                            database.endTransaction();
                            // database.close();
                        }
                    }


                }
            }
        }

        return result;
    }

    public Cursor rawQuery(String sql, String[]selectionArgs)
    {
        Cursor cursorResult = null;
        boolean isExistDatabase = checkExistsDataBase(DATABASE_NAME);
        {
            if(isExistDatabase)
            {
                database = this.getReadableDatabase();
                if(database != null && database.isOpen())
                {
                    try
                    {
                        database.beginTransaction();
                        cursorResult =  database.rawQuery(sql, selectionArgs);
                        database.setTransactionSuccessful();
                    }
                    finally {
                        if(database != null)
                        {
                            database.endTransaction();
                           // database.close();
                        }
                    }


                }
            }
        }


        return cursorResult;
    }

}
