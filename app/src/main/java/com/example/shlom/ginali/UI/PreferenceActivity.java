package com.example.shlom.ginali.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.shlom.ginali.R;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
    }
}
