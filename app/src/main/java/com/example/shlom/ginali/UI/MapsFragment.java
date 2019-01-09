package com.example.shlom.ginali.UI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.example.shlom.ginali.Database.Park;
import com.example.shlom.ginali.MyParksInfo;
import com.example.shlom.ginali.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private Context mContext;
    private GoogleMap mMap;
    private ArrayList<Park> parkArrayList;
    private MyParksInfo callback;
    private String route, parkRoute;
    private LatLng latLng;


    @Override
    public void onCreate(Bundle bundle) {
        Bundle getBundle = this.getArguments();
        if (getBundle != null) {
            Double mLat=getBundle.getDouble("lat");
            Double mLng=getBundle.getDouble("lng");
            route = getBundle.getString("route");
            parkRoute = getBundle.getString("parkname");
            latLng = new LatLng(mLat,mLng);
        }
        mContext = getContext();
        callback = (MyParksInfo)mContext;
        getMapAsync(this);
        super.onCreate(bundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        if(route!=null){
            drawRoute(route,mMap,parkRoute,latLng);
        }
        callback.getMap(mMap);

    }

    public void setNearbyParks(ArrayList parks,Context context,GoogleMap map,LatLng myLocation){
        mContext = context;
        mMap = map;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,12));
        parkArrayList = new ArrayList<>();
        parkArrayList = parks;
        for (int i = 0; i <parks.size() ; i++) {
            Park park = parkArrayList.get(i);
            LatLng latLng = new LatLng(park.getLatitude(),park.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("" + park.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    public void drawRoute(String poli,GoogleMap map,String place,LatLng latLng){
        mMap = map;
        mMap.clear();
        LatLng pin = latLng;
        mMap.addMarker(new MarkerOptions().position(pin).title("" + place));
        PolylineOptions polygonOptions = new PolylineOptions();
        polygonOptions.color(R.color.White);
        polygonOptions.width(10);
        polygonOptions.addAll(PolyUtil.decode(poli));
        mMap.addPolyline(polygonOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pin,12));


    }


}
