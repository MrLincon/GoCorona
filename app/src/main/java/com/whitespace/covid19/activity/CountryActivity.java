package com.whitespace.covid19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.whitespace.covid19.adapter.CountryAdapter;
import com.whitespace.covid19.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CountryActivity extends AppCompatActivity {

    ImageView back;
    RecyclerView recyclerView;
    String Country;

    Dialog popup;

    CountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        back = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.recyclerView);

        popup = new Dialog(CountryActivity.this);

        clearLightStatusBar(CountryActivity.this);

        SharedPreferences pref = getSharedPreferences("Country", MODE_PRIVATE);
        Country = pref.getString("country", "");

        if (Country.equals("")){
            back.setVisibility(View.INVISIBLE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://covid-193.p.rapidapi.com/countries")
                .get()
                .addHeader("X-RapidAPI-Key", "6baa475a0cmsh00bf94471309dbbp11633djsn80e92ce938c7")
                .addHeader("X-RapidAPI-Host", "covid-193.p.rapidapi.com")
                .build();


        //Get data from api
        getData(client, request);

    }

    private void getData(OkHttpClient client, Request request) {

        popup.setContentView(R.layout.popup_loading);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.show();
        popup.setCancelable(false);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                popup.dismiss();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseValue = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(responseValue);
                                JSONArray response = jsonObject.getJSONArray("response");
//                                JSONObject responseData = response.getJSONObject(0);
                                Log.d("TESTING", "run: " + response);

                                adapter = new CountryAdapter(response, CountryActivity.this);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CountryActivity.this);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(adapter);

                                popup.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
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

    @Override
    public void onBackPressed() {
        if (!Country.equals("")){
            super.onBackPressed();
        }

    }
}