package com.anutanetworks.ncxapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.anutanetworks.ncxapp.R;
import com.anutanetworks.ncxapp.services.AnutaRestClient;

import java.util.Map;

/**
 * Created by Aakash on 7/2/2015.
 */
public class SplashActivity extends Activity {
    public static final String PREFS_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread background = new Thread() {
            public void run() {

                try {

                    sleep(2 * 1000);
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    if (settings.getString("logged", "").equals("logged")) {
                        Map<String, String> settingValues = (Map<String, String>) settings.getAll();
                        AnutaRestClient.initializeClient(settingValues);
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                    }

                    finish();

                } catch (Exception e) {

                }
            }
        };


        background.start();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}