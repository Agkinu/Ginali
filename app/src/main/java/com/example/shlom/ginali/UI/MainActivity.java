package com.example.shlom.ginali.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.shlom.ginali.Database.DBHandler;
import com.example.shlom.ginali.Database.Park;
import com.example.shlom.ginali.MyParksInfo;
import com.example.shlom.ginali.ParksThread.GetParks;
import com.example.shlom.ginali.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyParksInfo {


    private FrameLayout frameLayout;
    private MapsFragment mapFragment;
    private static final String TAG = "MainActivity";
    private Boolean mLocationPremission = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_LOCATION_REQUEST = 1234;
    private Location mLocation;
    private Double lat,lng,routeLat,routeLng;
    private LatLng myLatLng;
    private FusedLocationProviderClient mFused;
    private GoogleMap mMap;
    private FloatingActionButton getNearbyLocations;
    private BottomNavigationView navigation;
    private String route, parkRoute;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Intent intent = getIntent();
        frameLayout = (FrameLayout)findViewById(R.id.mainFrameLayout);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getNearbyLocations = (FloatingActionButton)findViewById(R.id.floatingActionButtonGetNearby);
        mFused = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        getPermissions();
        getNearbyLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetParks getParks = new GetParks();
                getParks.getNearbyParks(myLatLng,MainActivity.this,MainActivity.this);
            }
        });
        if(intent!=null){
            try{
                route = intent.getStringExtra("route");
                parkRoute = intent.getStringExtra("parkName");
                routeLat = intent.getDoubleExtra("lat",0);
                routeLng = intent.getDoubleExtra("lng",0);
                if(route!=null){
                    navigation.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                    navigation.setSelectedItemId(R.id.navigation_dashboard);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_settings) {
            Intent intent = new Intent(MainActivity.this,PreferenceActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FavoritesFragment favoritesFragment = new FavoritesFragment();
                    setFragment(favoritesFragment);
                    getNearbyLocations.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    if(mapFragment==null){
                        mapFragment = new MapsFragment();
                    }
                    setFragment(mapFragment);
                    getNearbyLocations.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    ParksListFragment parksListFragment = new ParksListFragment();
                    setFragment(parksListFragment);
                    getNearbyLocations.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };


    private void setFragment(Fragment fragment){
        Bundle bundle = new Bundle();
        if (lat != null && lng != null) {
            bundle.putDouble("lat",lat);
            bundle.putDouble("lng",lng);
            fragment.setArguments(bundle);
        }
        if(parkRoute!=null){
            bundle.putDouble("lat",routeLat);
            bundle.putDouble("lng",routeLng);
            bundle.putString("route",route);
            bundle.putString("parkname",parkRoute);
            fragment.setArguments(bundle);
            routeLng = 0.0;
            routeLat = 0.0;
            parkRoute = null;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void getPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationPremission = true;
                    getDeviceLocation();
                }else{
                    ActivityCompat.requestPermissions(this,permissions,LOCATION_LOCATION_REQUEST);

                }
            }else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_LOCATION_REQUEST);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPremission = false;
        switch (requestCode){
            case LOCATION_LOCATION_REQUEST:{
                if(grantResults.length>0){
                    for (int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] !=PackageManager.PERMISSION_GRANTED){
                            mLocationPremission = false;
                            return;
                        }
                    }
                    mLocationPremission =true;
                    getDeviceLocation();

                }
            }
        }
    }

    private void getDeviceLocation(){

        try{
            if(mLocationPremission){
                Task location =  mFused.getLastLocation();
                location.addOnCompleteListener(MainActivity.this,new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()&&task.getResult()!=null){
                            mLocation = (Location) task.getResult();
                            lat = mLocation.getLatitude();
                            lng = mLocation.getLongitude();
                            myLatLng = new LatLng(lat,lng);
                            if(route==null){
                                navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                                navigation.setSelectedItemId(R.id.navigation_home);
                            }
                        }
                    }
                });
            }
            else{
                getPermissions();
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException" +e.getMessage());
        }
    }


    @Override
    public void getParksList(ArrayList parksList) {
        DBHandler dbHandler = new DBHandler(MainActivity.this);
        dbHandler.clearNearbyParksHistory();
        dbHandler.insertParksNearby(parksList);
        if(mMap!=null){
            mapFragment.setNearbyParks(parksList,MainActivity.this,mMap,myLatLng);
        }
        else{
            navigation.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
            navigation.setSelectedItemId(R.id.navigation_dashboard);
        }
    }

    @Override
    public void getMap(GoogleMap map) {
        mMap = map;
    }

    @Override
    public void getParkInfo(Park park) {
        Intent intent = new Intent(MainActivity.this,ParkActivity.class);
        intent.putExtra("parkName",park.getName());
        intent.putExtra("parkLat",park.getLatitude());
        intent.putExtra("parkLng",park.getLongitude());
        intent.putExtra("parkId",park.getPlaceId());
        intent.putExtra("parkAddress",park.getAddress());
        intent.putExtra("parkPhoto",park.getPhoto());
        intent.putExtra("parkRating",park.getRating());
        intent.putExtra("parkDistance",park.getDistance());
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        startActivity(intent);
    }




}
