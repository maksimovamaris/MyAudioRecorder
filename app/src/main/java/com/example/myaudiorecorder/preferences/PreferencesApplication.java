package com.example.myaudiorecorder.preferences;

import android.app.Application;
import android.preference.PreferenceManager;

public class PreferencesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.getDefaultSharedPreferences(this);
    }

}
