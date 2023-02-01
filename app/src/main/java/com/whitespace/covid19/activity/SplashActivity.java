package com.whitespace.covid19.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whitespace.covid19.R;

public class SplashActivity extends AppCompatActivity {

    String Country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        clearLightStatusBar(SplashActivity.this);

        SharedPreferences pref = getSharedPreferences("Country", MODE_PRIVATE);
        Country = pref.getString("country", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Country.equals("")){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this,CountryActivity.class));
                    finish();
                }

            }
        }, 2500);

    }

    public static void clearLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }
    }
}