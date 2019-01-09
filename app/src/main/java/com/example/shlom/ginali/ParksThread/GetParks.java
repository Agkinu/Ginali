package com.example.shlom.ginali.ParksThread;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.shlom.ginali.Database.DBHandler;
import com.example.shlom.ginali.MyParkRoute;
import com.example.shlom.ginali.MyParksInfo;
import com.example.shlom.ginali.Database.Park;
import com.example.shlom.ginali.R;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetParks {

    private Context mContext;
    private ArrayList<Park> parks;
    private MyParksInfo callback;
    private MyParkRoute callbackRoute;
    private Activity mActivity;
    private String url,nextpage,address,name,place_id,myphoto,website,phone,icon,distance,placeIdWeb;
    private Double lat,lng,placelat,placelng,rating;
    private boolean dogFriendly,favorite;






    public void getNearbyParks(final LatLng latLng, final Context context, final Activity activity){
        mContext = context;
        mActivity = activity;
        callback = (MyParksInfo) mContext;
        if(parks==null){
            parks = new ArrayList<>();
            url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latLng.latitude+","+latLng.longitude+"&language=iw&radius=1500&type=park&key="+context.getString(R.string.google_maps_key)+"";
        }else{
            url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken="+nextpage+"&key="+context.getString(R.string.google_maps_key)+"";
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject mainObject = new JSONObject(myresponse);
                                try{
                                    if(mainObject.getString("next_page_token")!=null){
                                        nextpage = mainObject.getString("next_page_token");
                                    }else {
                                        nextpage=null;
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                JSONArray results = mainObject.getJSONArray("results");
                                for (int i = 0; i <results.length(); i++) {
                                    JSONObject result = results.getJSONObject(i);
                                    String placeid = result.getString("place_id");
                                    String name = result.getString("name");
                                    try {
                                        address = result.getString("vicinity");
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    JSONObject geometry = result.getJSONObject("geometry");
                                    JSONObject location = geometry.getJSONObject("location");
                                    Double lat = location.getDouble("lat");
                                    Double lng = location.getDouble("lng");

                                    parks.add(new Park(name,lat,lng,placeid,address));
                                }
                                if(parks.size()<80&&nextpage!=null){
                                    getNearbyParks(latLng,mContext,activity);
                                }else{
                                    callback.getParksList(parks);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });

    }

    public void getParkInfo(final LatLng latLng, final String placeid, final Context context, final Activity activity) {
        mContext = context;
        mActivity = activity;
        callback = (MyParksInfo) mContext;
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeid+"&fields=name,rating,formatted_phone_number,photo,place_id,website,geometry,formatted_address,opening_hours&language=iw&key="+mContext.getString(R.string.google_maps_key)+"";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           try{
                               JSONObject object = new JSONObject(myresponse);
                               JSONObject  results = object.getJSONObject("result");
                               name = results.getString("name");
                               address = results.getString("formatted_address");
                               place_id = results.getString("place_id");
                               JSONObject geometry = results.getJSONObject("geometry");
                               JSONObject location = geometry.getJSONObject("location");
                               placelat = location.getDouble("lat");
                               placelng =location.getDouble("lng");
                               try {
                                   JSONArray photos = results.getJSONArray("photos");
                                   if(photos.length()>=1){
                                       for (int i = 0; i <photos.length() ; i++) {
                                           JSONObject photo = photos.getJSONObject(i);
                                           String string = photo.getString("photo_reference");
                                           if(i==0){
                                               myphoto = string;
                                           }else{
                                               myphoto =myphoto+" "+string;
                                           }
                                       }
                                   }else {
                                       myphoto = null;
                                   }
                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                               try {
                                   rating = results.getDouble("rating");
                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                               dogFriendly =false;
                               favorite=false;
                               Park park = new Park(name,placelat,placelng,place_id,address,myphoto,rating);
                               if(latLng!=null){
                                   getDistanceFromPlace(latLng,new LatLng(placelat,placelng),mContext,mActivity,park);
                               }else{
                                   Toast.makeText(mContext,mContext.getString(R.string.cant_find_location),Toast.LENGTH_LONG).show();
                               }

                        }catch (Exception e){
                               e.printStackTrace();
                           }
                        }
                    });

                }
            }
        });

    }


    public void getDistanceFromPlace(final LatLng whereImAt, final LatLng whereIWannaBe, final Context context, final Activity activity, final Park park){
        mContext = context;
        mActivity = activity;
        callback = (MyParksInfo) mContext;
        if(whereImAt!=null){
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+whereImAt.latitude+","+whereImAt.longitude+"&destinations="+whereIWannaBe.latitude+","+whereIWannaBe.longitude+"&key="+mContext.getString(R.string.google_maps_key)+"";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if(response.isSuccessful()){
                        final String myresponse =response.body().string();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject(myresponse);
                                    JSONArray results = object.getJSONArray("rows");
                                    JSONObject firstOb = results.getJSONObject(0);
                                    JSONArray secondAr = firstOb.getJSONArray("elements");
                                    JSONObject secondOb = secondAr.getJSONObject(0);
                                    try{
                                        JSONObject distanceOb = secondOb.getJSONObject("distance");
                                        try{
                                            distance = distanceOb.getString("text");
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    if(distance==null){
                                        distance=mContext.getString(R.string.too_far);
                                    }
                                    Park sendPark = new Park(park.getName(),park.getLatitude(),park.getLongitude(),park.getPlaceId(),park.getAddress(),
                                            park.getPhoto(),park.getRating(),distance);

                                    callback.getParkInfo(sendPark);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }
            });

        }
        else {
            Park sendPark = new Park(park.getName(),park.getLatitude(),park.getLongitude(),park.getPlaceId(),park.getAddress(),
                    park.getPhoto(),park.getRating(),null);

            callback.getParkInfo(sendPark);
        }

    }
    public void getRoute(final Park park, LatLng myPlace, final LatLng whereIWannaGo, final Context context, final Activity activity, String mode){
        mContext = context;
        mActivity = activity;
        callbackRoute = (MyParkRoute) mContext;
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+myPlace.latitude+","+myPlace.longitude+"&mode"+mode+"&destination="+whereIWannaGo.latitude+","+whereIWannaGo.longitude+"&key="+mContext.getString(R.string.google_maps_key)+"";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                JSONObject object = new JSONObject(myresponse);
                                String ok = object.getString("status");
                                if(ok.equals("OK")){
                                    JSONArray routesArray = object.getJSONArray("routes");
                                    JSONObject routesObject = routesArray.getJSONObject(0);
                                    JSONObject polylineOV = routesObject.getJSONObject("overview_polyline");
                                    String polyline = polylineOV.getString("points");
                                    callbackRoute.getRoute(polyline,park);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });
    }


}
