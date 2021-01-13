package com.tatasam.test.UI.countryList;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tatasam.test.R;
import com.tatasam.test.model.Country;

import java.util.ArrayList;
import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {
    private Context mcontext;
    ArrayList<Country> response;
    CountryListActivity.ListListener listListener;
    public CountryListAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }
    public CountryListAdapter(Context mcontext, CountryListActivity.ListListener listListener) {
        this.mcontext = mcontext;
        this.listListener = listListener;
    }
    public CountryListAdapter(Context mcontext, ArrayList<Country> response) {
        this.mcontext = mcontext;
        this.response = response;
    }
    public void setResponse(ArrayList<Country> response) {
        this.response = response;
    }
    @Override
    public CountryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_subitem_countrylist, parent, false);
        CountryListAdapter.ViewHolder holder = new CountryListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CountryListAdapter.ViewHolder holder, final int position) {
        if (response != null && response.size() > 0) {
            holder.labelCountryCode.setText("Code:  " + response.get(position).getCountryCode());
            holder.labelCountryName.setText("Name:  " + response.get(position).getCountryName());
            holder.labelCountryRegion.setText("Region:  " + response.get(position).getCountryRegion());
            if (listListener != null) {//listlisner is not null means this adapter used for countylist activity
                if (response.get(position).isLike())
                    holder.imgFavourite.setImageResource(R.drawable.like);
                else
                    holder.imgFavourite.setImageResource(R.drawable.dislike);
                holder.imgFavourite.setTag(position);
                holder.imgFavourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Fav", "Fav");
                        Integer pos = (Integer) view.getTag();
                        if (response.get(pos.intValue()).isLike())
                            holder.imgFavourite.setImageResource(R.drawable.dislike);
                        else
                            holder.imgFavourite.setImageResource(R.drawable.like);
                        listListener.onLikeCountry(pos.intValue(), response.get(pos.intValue()).isLike());
                    }
                });
            }
            else{//listlisner is  null means this adapter used for Fav countylist activity
                holder.imgFavourite.setImageResource(R.drawable.like);
            }
        }
    }
    @Override
    public int getItemCount() {
        return response != null ? response.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFavourite;
        TextView labelCountryCode, labelCountryName, labelCountryRegion;

        public ViewHolder(View itemView) {
            super(itemView);
            imgFavourite = itemView.findViewById(R.id.imgFavourite);
            labelCountryCode = itemView.findViewById(R.id.labelCountryCode);
            labelCountryName = itemView.findViewById(R.id.labelCountryName);
            labelCountryRegion = itemView.findViewById(R.id.labelCountryRegion);
        }
    }

}
