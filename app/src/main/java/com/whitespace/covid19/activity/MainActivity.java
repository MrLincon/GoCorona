package com.whitespace.covid19.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whitespace.covid19.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView dateTV, countryTV, populationTV, newCasesTV, activeCasesTV,
            criticalCasesTV, recoveredCasesTV, totalCasesTV, newDeathsTV, totalDeathsTV, totalTestsTV;

    String continent, population, country, day, time;
    String newCases, activeCases, criticalCases, recoveredCases, OneMilPopulationCases, totalCases;
    String newDeaths, OneMilPopulationDeaths, totalDeaths;
    String OneMilPopulationTests, totalTests;

    ImageView refresh, settings;

    Request request;
    OkHttpClient client;
    String Country;

    Dialog popup, popupCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTV = findViewById(R.id.date);
        countryTV = findViewById(R.id.country);
        populationTV = findViewById(R.id.population);
        newCasesTV = findViewById(R.id.new_cases);
        activeCasesTV = findViewById(R.id.active_cases);
        criticalCasesTV = findViewById(R.id.critical_cases);
        recoveredCasesTV = findViewById(R.id.recovered_cases);
        totalCasesTV = findViewById(R.id.total_cases);
        newDeathsTV = findViewById(R.id.new_deaths);
        totalDeathsTV = findViewById(R.id.total_deaths);
        totalTestsTV = findViewById(R.id.total_tests);

        refresh = findViewById(R.id.refresh);
        settings = findViewById(R.id.settings);

        popup = new Dialog(MainActivity.this);
        popupCountry = new Dialog(MainActivity.this);

        clearLightStatusBar(MainActivity.this);

        client = new OkHttpClient();

        SharedPreferences pref = getSharedPreferences("Country", MODE_PRIVATE);
        Country = pref.getString("country", "");

        if (!Country.equals("")){
            request = new Request.Builder()
                    .url("https://covid-193.p.rapidapi.com/statistics?country="+Country)
                    .get()
                    .addHeader("X-RapidAPI-Key", "6baa475a0cmsh00bf94471309dbbp11633djsn80e92ce938c7")
                    .addHeader("X-RapidAPI-Host", "covid-193.p.rapidapi.com")
                    .build();

            //Get data from api
            getData(client, request);
        }else {

        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryTV.setText("Country");
                populationTV.setText("0");
                newCasesTV.setText("0");
                activeCasesTV.setText("0");
                criticalCasesTV.setText("0");
                recoveredCasesTV.setText("0");
                totalCasesTV.setText("0");
                newDeathsTV.setText("0");
                totalDeathsTV.setText("0");
                totalTestsTV.setText("0");
                getData(client, request);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

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
                                JSONObject responseData = response.getJSONObject(0);
                                Log.d("TESTING", "run: " + responseData);

                                continent = responseData.getString("continent");
                                population = responseData.getString("population");
                                country = responseData.getString("country");
                                day = responseData.getString("day");
                                time = responseData.getString("time");

                                JSONObject cases = responseData.getJSONObject("cases");
                                JSONObject deaths = responseData.getJSONObject("deaths");
                                JSONObject tests = responseData.getJSONObject("tests");

                                newCases = cases.getString("new");
                                activeCases = cases.getString("active");
                                criticalCases = cases.getString("critical");
                                recoveredCases = cases.getString("recovered");
                                OneMilPopulationCases = cases.getString("1M_pop");
                                totalCases = cases.getString("total");

                                newDeaths = deaths.getString("new");
                                OneMilPopulationDeaths = deaths.getString("1M_pop");
                                totalDeaths = deaths.getString("total");

                                OneMilPopulationTests = tests.getString("1M_pop");
                                totalTests = tests.getString("total");


                                //Set data to text fields
                                if (!country.equals("null")) {
                                    countryTV.setText(country);
                                }

                                if (!population.equals("null")) {
                                    populationTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(population)));
                                }

                                if (!day.equals("null")) {
                                    dateTV.setText(day);
                                }

                                if (!newCases.equals("null")) {
                                    newCasesTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(newCases)));
                                }

                                if (!activeCases.equals("null")) {
                                    activeCasesTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(activeCases)));
                                }

                                if (!criticalCases.equals("null")) {
                                    criticalCasesTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(criticalCases)));
                                }

                                if (!recoveredCases.equals("null")) {
                                    recoveredCasesTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(recoveredCases)));
                                }

                                if (!totalCases.equals("null")) {
                                    totalCasesTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(totalCases)));
                                }


                                if (!newDeaths.equals("null")) {
                                    newDeathsTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(newDeaths)));
                                }

                                if (!totalDeaths.equals("null")) {
                                    totalDeathsTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(totalDeaths)));
                                }


                                if (!totalTests.equals("null")) {
                                    totalTestsTV.setText(DecimalFormat.getNumberInstance().format(Integer.parseInt(totalTests)));
                                }

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
}