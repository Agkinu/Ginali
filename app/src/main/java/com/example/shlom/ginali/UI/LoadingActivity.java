package com.example.shlom.ginali.UI;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shlom.ginali.Database.Constants;
import com.example.shlom.ginali.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

public class LoadingActivity extends AppCompatActivity {


    private String userEmail;
    private static final String TAG = "LoadingActivity";
    private CardView loginGuest;
    private LoginButton loginButtonFacebook;
    private CallbackManager callbackManager;
    private LottieAnimationView mLottieAnimationView;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_LOCATION_REQUEST = 1234;
    private Boolean mLocationPremission = false;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        loginGuest = (CardView)findViewById(R.id.loginButtonGuest);
        loginButtonFacebook = (LoginButton)findViewById(R.id.login_button);
        mLottieAnimationView = findViewById(R.id.animation);
        mLottieAnimationView.setRepeatCount(Animation.INFINITE);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(LoadingActivity.this);
        boolean loggedin = mPreferences.getBoolean("loggedIn",false);
        if(loggedin){
            Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
            startActivity(intent);
        }
        getPermissions();
        callbackManager = CallbackManager.Factory.create();
        loginButtonFacebook.setReadPermissions(Arrays.asList(Constants.EMAIL,Constants.PUBLICPROFILE));
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        getData(object);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,link");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        loginGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                int randomNum = rand.nextInt(1000000);
                userEmail = "Guest "+randomNum;
                mEditor = mPreferences.edit();
                mEditor.putString("email",userEmail);
                mEditor.putBoolean("loggedIn",true);
                mEditor.apply();
                Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getData(JSONObject o){
        try {
            URL profilePic = new URL("https://graph.facebook.com/"+o.getString("id")+"/picture?width=250&hight=250");
            userEmail = o.getString("email");
            mEditor = mPreferences.edit();
            mEditor.putString("email",userEmail);
            mEditor.putString("picurl",profilePic.toString());
            mEditor.putBoolean("loggedIn",true);
            mEditor.apply();
            Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
            startActivity(intent);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationPremission = true;
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

                }
            }
        }
    }


}
