package com.example.shlom.ginali.UI;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shlom.ginali.Database.DBHandler;
import com.example.shlom.ginali.Database.Park;
import com.example.shlom.ginali.Database.Review;
import com.example.shlom.ginali.MyParkRoute;
import com.example.shlom.ginali.MyParksInfo;
import com.example.shlom.ginali.ParksThread.GetParks;
import com.example.shlom.ginali.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ParkActivity extends AppCompatActivity implements MyParkRoute {

    private String address,name,place_id,myphoto,distance;
    private Double lat,lng,rating;
    private String[]parkPhotos;
    private ImageView mParkImageView;
    private TextView mTextViewAddress,mTextViewDistance;
    private RatingBar mRatingBar,mRatingBarReview;
    private ViewPager mViewPager;
    private EditText mEditTextReview;
    private ArrayList<String> handicapArrayList,dogArrayList,parkingArrayList,playgroundArrayList;
    private CheckBox checkBoxDogs,checkBoxHandicap,checkBoxParking,checkBoxPlayground;
    private FloatingActionButton saveButton,mFloatingActionButtonDelete,mFloatingActionButtonShare,
            mFloatingActionButtonLocation,mFloatingActionButtonDirections;
    private ImageView imageViewBackToMain;
    private SharedPreferences mPreferences;
    private RadioButton radioButtonHandicap,radioButtonParking,radioButtonDog,radioButtonPlayground;
    private Button mButtonSendReview;
    private ArrayList<Review> reviewArrayList;
    private ParkListAdapterReviews parkListAdapterReviews;
    private RecyclerView recyclerView;
    private LatLng mLatLng;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.park_activity);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.parkToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextViewAddress = (TextView)findViewById(R.id.textViewParkInfoAddress);
        mTextViewDistance = (TextView)findViewById(R.id.textViewParkInfoDistance);
        mRatingBar = (RatingBar)findViewById(R.id.ratingBar);
        checkBoxDogs = (CheckBox)findViewById(R.id.checkBoxDogFriendly);
        checkBoxHandicap = (CheckBox)findViewById(R.id.checkBoxHandicapFriendly);
        checkBoxParking = (CheckBox)findViewById(R.id.checkBoxParking);
        checkBoxPlayground = (CheckBox)findViewById(R.id.checkBoxPlayground);
        saveButton = (FloatingActionButton)findViewById(R.id.floatingActionButtonSave);
        mFloatingActionButtonShare = (FloatingActionButton) findViewById(R.id.floatingActionButtonShare);
        mFloatingActionButtonDelete= (FloatingActionButton) findViewById(R.id.floatingActionButtonDelete);
        mFloatingActionButtonLocation= (FloatingActionButton) findViewById(R.id.floatingActionButtonLocation);
        mFloatingActionButtonDirections = (FloatingActionButton) findViewById(R.id.floatingActionButtonNavigate);
        radioButtonDog = (RadioButton) findViewById(R.id.radioButtonDog);
        radioButtonHandicap = (RadioButton) findViewById(R.id.radioButtonHandicap);
        radioButtonParking = (RadioButton) findViewById(R.id.radioButtonParking);
        radioButtonPlayground = (RadioButton) findViewById(R.id.radioButtonAmusment);
        mEditTextReview = (EditText)findViewById(R.id.editTextReview);
        mRatingBarReview = (RatingBar)findViewById(R.id.ratingBarReview);
        mButtonSendReview = (Button)findViewById(R.id.buttonSendReview);
        recyclerView = findViewById(R.id.recyclerViewReview);
        Intent intent = getIntent();
        getAParkInfo(intent);
        displayParkDataFirebaseReviews();
        Park park = new Park(name,lat,lng,place_id,address,myphoto,rating);
        DBHandler dbHandler = new DBHandler(ParkActivity.this);
        Park favorite = dbHandler.checkIfFavorite(new LatLng(lat,lng));
        if(favorite.isFavorite()){
            saveButton.setImageDrawable(getDrawable(R.drawable.ic_action_name_favorite));
        }else {
            saveButton.setImageDrawable(getDrawable(R.drawable.ic_action_name));
        }

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(ParkActivity.this));
        toolbar.setTitle(name);
        if(myphoto!=null){
            parkPhotos = myphoto.split("\\s+");
            mParkImageView = (ImageView)findViewById(R.id.imageviewPark2);
            String photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+parkPhotos[0]+"&key="+getString(R.string.google_maps_key)+"";
            Uri googleUri = Uri.parse(photo);
            Picasso.with(this).load(googleUri).fit().centerCrop().into(mParkImageView);
        }
        mTextViewAddress.setText(address);
        mRatingBar.setRating(rating.floatValue());
        try {
            mTextViewDistance.setText(getString(R.string.distance)+": "+distance);
        }catch (Exception e){
            e.printStackTrace();
        }
        mViewPager = (ViewPager)findViewById(R.id.viewpagerPictures);
        displayParkDataFirebase();
        PageViewImages pageViewImages = new PageViewImages(this,parkPhotos);
        mViewPager.setAdapter(pageViewImages);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Park park = new Park(name,lat,lng,place_id,address,myphoto,rating);
                DBHandler dbHandler = new DBHandler(ParkActivity.this);
                dbHandler.insertParkFavorite(park);
                Park favorite = dbHandler.checkIfFavorite(new LatLng(lat,lng));
                if(favorite.isFavorite()){
                    saveButton.setImageDrawable(getDrawable(R.drawable.ic_action_name_favorite));
                }else {
                    saveButton.setImageDrawable(getDrawable(R.drawable.ic_action_name));
                }

            }
        });
        mFloatingActionButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+lat+","+lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        mFloatingActionButtonDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRouteType();
            }
        });

        mFloatingActionButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                info intent
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT,name);
                share.putExtra(Intent.EXTRA_TEXT,"https://www.google.com/maps/search/?api=1&query="+lat+","+lng);
                startActivity(Intent.createChooser(share,"Share Via"));
            }
        });
        mButtonSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveParkData();
            }
        });
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
            Intent intent = new Intent(ParkActivity.this,PreferenceActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveParkData(){
        Calendar cal = Calendar.getInstance();

        int getSecond = cal.get(Calendar.SECOND);
        int getMinute = cal.get(Calendar.MINUTE);
        int getHourofday = cal.get(Calendar.HOUR_OF_DAY);
        int getYear = cal.get(Calendar.YEAR);
        int getMonth = cal.get(Calendar.MONTH);
        int getDayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ParkActivity.this);
        String loggedIn = mPreferences.getString("email",null);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(place_id);
        DatabaseReference myUser = myRef.child(loggedIn);
        float getRating = mRatingBarReview.getRating();
        String getReview = mEditTextReview.getText().toString();
        boolean getParking = radioButtonParking.isChecked();
        boolean getHandicap = radioButtonHandicap.isChecked();
        boolean getDog = radioButtonDog.isChecked();
        boolean getPlayground = radioButtonPlayground.isChecked();
        DatabaseReference rating = myUser.child("rating");
        DatabaseReference review = myUser.child("review");
        DatabaseReference handicapFriendly = myUser.child("handicapFriendly");
        DatabaseReference dogFriendly = myUser.child("dogFriendly");
        DatabaseReference parking = myUser.child("parking");
        DatabaseReference playground = myUser.child("playground");
        DatabaseReference second = myUser.child("second");
        DatabaseReference minute = myUser.child("minute");
        DatabaseReference hourofday = myUser.child("hourofday");
        DatabaseReference month = myUser.child("month");
        DatabaseReference dayofmonth = myUser.child("dayofmonth");
        DatabaseReference year = myUser.child("year");
        second.setValue(getSecond);
        minute.setValue(getMinute);
        hourofday.setValue(getHourofday);
        month.setValue(getMonth);
        dayofmonth.setValue(getDayofmonth);
        year.setValue(getYear);
        rating.setValue(getRating);
        review.setValue(getReview);
        handicapFriendly.setValue(getHandicap);
        dogFriendly.setValue(getDog);
        parking.setValue(getParking);
        playground.setValue(getPlayground);
    }

    public void displayParkDataFirebase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(place_id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //set boolean to something
                handicapArrayList = new ArrayList();
                dogArrayList = new ArrayList();
                playgroundArrayList = new ArrayList();
                parkingArrayList = new ArrayList();


                Iterable<DataSnapshot> myinfo = dataSnapshot.getChildren();
                for(DataSnapshot dataSnapshot1:myinfo){
                    try {
                        String handicap = dataSnapshot1.child("handicapFriendly").getValue().toString();
                        if(handicap!=null){
                            handicapArrayList.add(handicap);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        String dogs = dataSnapshot1.child("dogFriendly").getValue().toString();
                        if(dogs!=null){
                            dogArrayList.add(dogs);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        String playground = dataSnapshot1.child("playground").getValue().toString();
                        if(playground!=null){
                            playgroundArrayList.add(playground);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        String parking = dataSnapshot1.child("parking").getValue().toString();
                        if(parking!=null){
                            parkingArrayList.add(parking);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
                boolean w = getBooleanValue(parkingArrayList);
                boolean x = getBooleanValue(handicapArrayList);
                boolean y = getBooleanValue(dogArrayList);
                boolean z = getBooleanValue(playgroundArrayList);
                if(w){
                    checkBoxParking.setChecked(true);
                }
                if(x){
                    checkBoxHandicap.setChecked(true);
                }
                if(y){
                    checkBoxDogs.setChecked(true);
                }
                if(z){
                    checkBoxPlayground.setChecked(true);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void displayParkDataFirebaseReviews(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(place_id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //set boolean to something
                reviewArrayList = new ArrayList<>();
                Iterable<DataSnapshot> myinfo = dataSnapshot.getChildren();
                for(DataSnapshot dataSnapshot1:myinfo){
                    try {
                        String getReview = dataSnapshot1.child("review").getValue().toString();
                        float rating = Float.parseFloat(dataSnapshot1.child("rating").getValue().toString());
                        String second = dataSnapshot1.child("second").getValue().toString();
                        String minute = dataSnapshot1.child("minute").getValue().toString();
                        String hourofday = dataSnapshot1.child("hourofday").getValue().toString();
                        String month = dataSnapshot1.child("month").getValue().toString();
                        String dayofmonth = dataSnapshot1.child("dayofmonth").getValue().toString();
                        String year = dataSnapshot1.child("year").getValue().toString();
                        String date = dayofmonth+"/"+month+"/"+year+" "+hourofday+":"+minute+":"+second+"";
                        Review review = new Review(getReview,date,rating);
                        reviewArrayList.add(review);


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                parkListAdapterReviews = new ParkListAdapterReviews(ParkActivity.this,ParkActivity.this,reviewArrayList);
                recyclerView.setAdapter(parkListAdapterReviews);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void getAParkInfo(Intent intent){
        name = intent.getStringExtra("parkName");
        lat = intent.getDoubleExtra("parkLat",0);
        lng = intent.getDoubleExtra("parkLng",0);
        place_id = intent.getStringExtra("parkId");
        address = intent.getStringExtra("parkAddress");
        myphoto = intent.getStringExtra("parkPhoto");
        rating = intent.getDoubleExtra("parkRating",0);
        distance = intent.getStringExtra("parkDistance");
        mLatLng = new LatLng(intent.getDoubleExtra("lat",0),intent.getDoubleExtra("lng",0));
    }

    public boolean getBooleanValue(ArrayList arrayList){
        double test;
        double count = 0;
        boolean checkBox = false;
        for (int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i).equals("true")){
                test =1 ;
            }
            else{
                test =0 ;
            }
            count += test;
        }
        double value = count/arrayList.size();
        if(value>0.6){
           checkBox = true;
        }

        return checkBox;
    }

    public void getRouteType(){
        final Dialog dialog = new Dialog(ParkActivity.this);
        dialog.setContentView(R.layout.route_dialog);
        FloatingActionButton floatingActionButtonCar = dialog.findViewById(R.id.floatingActionButtonCar);
        FloatingActionButton floatingActionButtonWalk = dialog.findViewById(R.id.floatingActionButtonWalk);
        FloatingActionButton floatingActionButtonBicycle = dialog.findViewById(R.id.floatingActionButtonBicycle);
        FloatingActionButton floatingActionButtonBus = dialog.findViewById(R.id.floatingActionButtonBus);
        floatingActionButtonCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Park park = new Park(name,lat,lng,place_id,address,myphoto,rating);
                GetParks getParks = new GetParks();
                getParks.getRoute(park,mLatLng,new LatLng(lat,lng),ParkActivity.this,ParkActivity.this,"driving");
                dialog.dismiss();

            }
        });
        floatingActionButtonWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Park park = new Park(name,lat,lng,place_id,address,myphoto,rating);
                GetParks getParks = new GetParks();
                getParks.getRoute(park,mLatLng,new LatLng(lat,lng),ParkActivity.this,ParkActivity.this,"walking");
                dialog.dismiss();

            }
        });
        floatingActionButtonBicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Park park = new Park(name,lat,lng,place_id,address,myphoto,rating);
                GetParks getParks = new GetParks();
                getParks.getRoute(park,mLatLng,new LatLng(lat,lng),ParkActivity.this,ParkActivity.this,"bicycling");
                dialog.dismiss();

            }
        });
        floatingActionButtonBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Park park = new Park(name,lat,lng,place_id,address,myphoto,rating);
                GetParks getParks = new GetParks();
                getParks.getRoute(park,mLatLng,new LatLng(lat,lng),ParkActivity.this,ParkActivity.this,"transit");
                dialog.dismiss();

            }
        });

        dialog.show();
    }




    @Override
    public void getRoute(String route, Park park) {
        Intent intent = new Intent(ParkActivity.this,MainActivity.class);
        intent.putExtra("route",route);
        intent.putExtra("parkName",park.getName());
        intent.putExtra("lat",park.getLatitude());
        intent.putExtra("lng",park.getLongitude());
        startActivity(intent);
    }
}
