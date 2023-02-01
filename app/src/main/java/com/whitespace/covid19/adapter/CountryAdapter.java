package com.whitespace.covid19.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.whitespace.covid19.R;
import com.whitespace.covid19.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CustomViewHolder> {

    private JSONArray dataList;
    private Context context;

    public CountryAdapter(JSONArray dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.CustomViewHolder holder, int position) {
        try {
            holder.country.setText((CharSequence) dataList.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("Country",MODE_PRIVATE);
                SharedPreferences.Editor pref = sharedPreferences.edit();
                try {
                    pref.putString("country",dataList.get(position).toString().toLowerCase());
                    pref.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((Activity)context).startActivity(new Intent(context, MainActivity.class));
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.length();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView country;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card);
            country = itemView.findViewById(R.id.country);
        }
    }
}
