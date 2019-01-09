package com.example.shlom.ginali.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBHandler {
    private DBHelper helper;
    private String  name,placeId,latitude,longitude,photo;
    private ArrayList<Park> arrayListPark;
    private Double latitude1,longitude1,rating;
    private boolean favorite;
    private Park parkInfo;


    public DBHandler(Context context){
        helper = new DBHelper(context, DBConstunts.DATABASE_NAME, null, DBConstunts.DATABASE_VERSION);
    }

    public DBHandler(){}

    public void insertParksNearby(ArrayList myParks){
        long id = 0;
        SQLiteDatabase db = null;
        arrayListPark = new ArrayList<>();
        arrayListPark = myParks;
        for (int i = 0; i < arrayListPark.size(); i++) {
            Park park = arrayListPark.get(i);
            try {
                db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DBConstunts.COLUMN_NAME_NAME,park.getName());
                values.put(DBConstunts.COLUMN_NAME_PLACEID,park.getPlaceId());
                values.put(DBConstunts.COLUMN_NAME_LATITUDE,park.getLatitude());
                values.put(DBConstunts.COLUMN_NAME_LONGITUDE,park.getLongitude());
                values.put(DBConstunts.COLUMN_NAME_ADDRESS,park.getAddress());
                values.put(DBConstunts.COLUMN_NAME_ID,id = db.insert(DBConstunts.TABLE_NAME_SEARCH, null, values));
                db.insert(DBConstunts.TABLE_NAME_SEARCH,null,values);
            }catch (SQLException e){
                e.getCause();
            }finally {
                if(db.isOpen());
                db.close();
            }
        }
    }

    public void insertParkFavorite(Park park){
        Park fav = checkIfFavorite(new LatLng(park.getLatitude(),park.getLongitude()));
        if (!fav.isFavorite()){
            long id = 0;
            SQLiteDatabase db = null;
            try {
                db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DBConstunts.COLUMN_NAME_NAME,park.getName());
                values.put(DBConstunts.COLUMN_NAME_PLACEID,park.getPlaceId());
                values.put(DBConstunts.COLUMN_NAME_LATITUDE,park.getLatitude());
                values.put(DBConstunts.COLUMN_NAME_LONGITUDE,park.getLongitude());
                values.put(DBConstunts.COLUMN_NAME_ADDRESS,park.getAddress());
                values.put(DBConstunts.COLUMN_NAME_PHOTO,park.getPhoto());
                values.put(DBConstunts.COLUMN_NAME_RATING,park.getRating().toString());
                values.put(DBConstunts.COLUMN_NAME_ID,id = db.insert(DBConstunts.TABLE_NAME_FAVORITES, null, values));
                db.insert(DBConstunts.TABLE_NAME_FAVORITES,null,values);
            }catch (SQLException e){
                e.getCause();
            }finally {
                if(db.isOpen());
                db.close();
            }
        }
        else {
            try{

                deleteParkFavorites(String.valueOf(fav.getId()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

//    public MyPlace selectMyPlaceHistory(String _id) {
//        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = null;
//        try {
//            cursor = db.query(DBConstunts.TABLE_NAME_FAVORITE, null, "_id=?", new String[]{_id},
//                    null, null, null, null);
//        } catch (SQLException e) {
//            e.getCause();
//        }
//        cursor.moveToFirst();
//        MyPlace myPlace;
//        int id = cursor.getInt(0);
//        String name = cursor.getString(1);
//        String address = cursor.getString(2);
//        try{
//            latitude = cursor.getString(3);
//            latitude1 = Double.valueOf(latitude);
//            if(latitude ==null){
//                latitude1 = 0.0;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try{
//            longitude = cursor.getString(4);
//            longitude1 = Double.valueOf(longitude);
//            if(longitude ==null){
//                longitude1 = 0.0;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        String placeId = cursor.getString(5);
//
//        myPlace = new MyPlace(id,
//                name,
//                placeId,
//                latitude1,
//                longitude1,
//                address);
//
//        return myPlace;
//    }
//
//
//
    public ArrayList<Park> selectAllMyParksNearby(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DBConstunts.TABLE_NAME_SEARCH, null, null, null, null, null,
                    null);
        }catch (SQLException e) {
            e.getCause();
        }
        ArrayList<Park> table = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);

            try{
                latitude = cursor.getString(3);
                latitude1 = Double.valueOf(latitude);
                if(latitude ==null){
                    latitude1 = 0.0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                longitude = cursor.getString(4);
                longitude1 = Double.valueOf(longitude);
                if(longitude ==null){
                    longitude1 = 0.0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            String placeId = cursor.getString(5);

            table.add(new Park(name,
                    latitude1,
                    longitude1,
                    placeId,
                    address,
                    id));
        }

        return table;
    }

    public ArrayList<Park> selectAllMyParksFavorites(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DBConstunts.TABLE_NAME_FAVORITES, null, null, null, null, null,
                    null);
        }catch (SQLException e) {
            e.getCause();
        }
        ArrayList<Park> table = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);

            try{
                latitude = cursor.getString(3);
                latitude1 = Double.valueOf(latitude);
                if(latitude ==null){
                    latitude1 = 0.0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                longitude = cursor.getString(4);
                longitude1 = Double.valueOf(longitude);
                if(longitude ==null){
                    longitude1 = 0.0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            String placeId = cursor.getString(5);

            try{
                photo = cursor.getString(6);
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                rating = Double.valueOf(cursor.getString(7));
            }catch (Exception e){
                e.printStackTrace();
            }

            table.add(new Park(name,
                    latitude1,
                    longitude1,
                    placeId,
                    address,
                    id,photo,rating));
        }

        return table;
    }

    public Park checkIfFavorite(LatLng latLng){
        arrayListPark = new ArrayList<>();
        favorite = false;
        parkInfo = new Park(0,false);
        arrayListPark = selectAllMyParksFavorites();
        for (int i = 0; i < arrayListPark.size(); i++) {
            Park park = arrayListPark.get(i);
            Double lat = park.getLatitude();
            if(latLng.latitude==lat){
                Double lng = park.getLongitude();
                if (latLng.longitude==lng){
                    favorite = true;
                    parkInfo = new Park(park.getId(),favorite);
                    break;
                }
            }

        }
        return parkInfo;
    }


    public boolean deleteParkFavorites(String _id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.delete(DBConstunts.TABLE_NAME_FAVORITES, "_id=?", new String[]{_id});
        } catch (SQLException e) {
        } finally {
            if (db.isOpen()) ;
            db.close();
        }
        return true;
    }

    public boolean clearNearbyParksHistory(){
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.delete(DBConstunts.TABLE_NAME_SEARCH, null, null);
            db.delete(DBConstunts.TABLE_NAME_FAVORITES, null, null);

        } catch (SQLException e) {
        } finally {
            if (db.isOpen()) ;
            db.close();
        }
        return true;
    }



}
