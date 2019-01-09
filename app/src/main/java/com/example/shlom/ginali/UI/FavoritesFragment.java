package com.example.shlom.ginali.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shlom.ginali.Database.DBHandler;
import com.example.shlom.ginali.Database.Park;
import com.example.shlom.ginali.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class FavoritesFragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerViewParks;
    private ParkListAdapterFavorites parkListAdapter;
    private ArrayList<Park> parkArrayList;
    private Context mContext;
    private Activity mActivity;
    private LatLng mLatLng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment,container,false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Double mLat=bundle.getDouble("lat");
            Double mLng=bundle.getDouble("lng");
            mLatLng = new LatLng(mLat,mLng);
        }
        else{
            mLatLng = null;
        }
        mActivity = getActivity();
        mContext = getContext();
        parkArrayList = new ArrayList<>();
        DBHandler handler = new DBHandler(mContext);
        parkArrayList = handler.selectAllMyParksFavorites();
        recyclerViewParks =view.findViewById(R.id.recyclerViewParksFavorites);
        recyclerViewParks.setHasFixedSize(false);
        recyclerViewParks.setLayoutManager(new LinearLayoutManager(mContext));
        parkListAdapter = new ParkListAdapterFavorites(mContext,mActivity,parkArrayList,mLatLng);
        recyclerViewParks.setAdapter(parkListAdapter);
        return view;


    }


}
