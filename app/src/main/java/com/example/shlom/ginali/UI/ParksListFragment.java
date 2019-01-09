package com.example.shlom.ginali.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shlom.ginali.Database.DBHandler;
import com.example.shlom.ginali.Database.Park;
import com.example.shlom.ginali.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ParksListFragment extends android.support.v4.app.Fragment {
    private RecyclerView recyclerViewParks;
    private ParkListAdapterNearby parkListAdapter;
    private ArrayList<Park> parkArrayList;
    private Context mContext;
    private Activity mActivity;
    private LatLng mLatLng;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parks_list,container,false);
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
        parkArrayList = handler.selectAllMyParksNearby();
        recyclerViewParks =view.findViewById(R.id.recyclerViewParks);
        recyclerViewParks.setHasFixedSize(false);
        recyclerViewParks.setLayoutManager(new GridLayoutManager(mContext, 2));
        parkListAdapter = new ParkListAdapterNearby(mContext,mActivity,parkArrayList,mLatLng);
        recyclerViewParks.setAdapter(parkListAdapter);
        return view;
    }

}
