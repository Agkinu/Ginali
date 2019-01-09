package com.example.shlom.ginali;

import com.example.shlom.ginali.Database.Park;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface MyParksInfo {
    void getParksList(ArrayList parksList);
    void getMap(GoogleMap map);
    void getParkInfo(Park park);
}
