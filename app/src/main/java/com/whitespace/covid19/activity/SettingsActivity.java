package com.whitespace.covid19.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.whitespace.covid19.R;

public class SettingsActivity extends AppCompatActivity {

    private ImageView back_btn;
    private CardView changeCountry, rate, share, feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back_btn = findViewById(R.id.back_btn);
        changeCountry = findViewById(R.id.country);
        rate = findViewById(R.id.rate);
        share = findViewById(R.id.share);
        feedback = findViewById(R.id.feedback);

        clearLightStatusBar(SettingsActivity.this);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        changeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, CountryActivity.class));
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("Comming soon..." + getPackageName())));
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName();

                String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", link);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SettingsActivity.this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "someone@gmail.com"));

                try {
                    startActivity(Intent.createChooser(feedback, "Choose an e-mail client"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SettingsActivity.this, "There is no e-mail clint installed!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public static void clearLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorCard));
        }
    }

}