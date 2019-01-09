package com.example.shlom.ginali.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public DBHelper(Context context){
        super(context, DBConstunts.DATABASE_NAME,null,DBConstunts.DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            String cmd1 = "CREATE TABLE " + DBConstunts.TABLE_NAME_SEARCH +
                    " (" + DBConstunts.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstunts.COLUMN_NAME_NAME + " TEXT, "
                    + DBConstunts.COLUMN_NAME_ADDRESS + " TEXT, "
                    + DBConstunts.COLUMN_NAME_LATITUDE + " TEXT, "
                    + DBConstunts.COLUMN_NAME_LONGITUDE + " TEXT, "
                    + DBConstunts.COLUMN_NAME_PLACEID + " TEXT" + ");";

            String cmd2 = "CREATE TABLE " + DBConstunts.TABLE_NAME_FAVORITES +
                    " (" + DBConstunts.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstunts.COLUMN_NAME_NAME + " TEXT, "
                    + DBConstunts.COLUMN_NAME_ADDRESS + " TEXT, "
                    + DBConstunts.COLUMN_NAME_LATITUDE + " TEXT, "
                    + DBConstunts.COLUMN_NAME_LONGITUDE + " TEXT, "
                    + DBConstunts.COLUMN_NAME_PLACEID + " TEXT, "
                    + DBConstunts.COLUMN_NAME_PHOTO + " TEXT, "
                    + DBConstunts.COLUMN_NAME_RATING + " TEXT" + ");";

            db.execSQL(cmd2);
            db.execSQL(cmd1);
        } catch (SQLException e){
                e.getCause();
            }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
