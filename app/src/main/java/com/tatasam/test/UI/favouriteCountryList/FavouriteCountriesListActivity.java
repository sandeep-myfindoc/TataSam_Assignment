package com.tatasam.test.UI.favouriteCountryList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.tatasam.test.R;
import com.tatasam.test.UI.SuperActivity;
import com.tatasam.test.UI.TataSamApplication;
import com.tatasam.test.UI.countryList.CountryListActivity;
import com.tatasam.test.UI.countryList.CountryListAdapter;
import com.tatasam.test.broadcastreceiver.ConnectivityReceiver;
import com.tatasam.test.databinding.ActivityFavouriteCountriesListBinding;
import com.tatasam.test.model.Country;
import com.tatasam.test.util.CommonUtility;
import com.tatasam.test.viewModel.FavouriteCountryListViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FavouriteCountriesListActivity extends SuperActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ActivityFavouriteCountriesListBinding binding;
    private FavouriteCountryListViewModel viewModel;
    private CountryListAdapter adapter;
    private ConnectivityReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_favourite_countries_list);
        viewModel = ViewModelProviders.of(this).get(FavouriteCountryListViewModel.class);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
        updateToolbar(getResources().getString(R.string.title_fav_country_list));
        receiver = new ConnectivityReceiver();
        viewModel.init();
        initRecyclerView();
        TataSamApplication.getInstance().setConnectivityListener(this);//assign refence of current activity to listener of Connectivity receiver
        if(new CommonUtility(this).checkInternetConnection()) {
            fetchFavCountriesList();
        }
        else{
            Snackbar snackbar = Snackbar
                    .make(binding.coordinatorLayout, getResources().getString(R.string.msg_offline), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver,intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    private void initRecyclerView() {
        adapter = new CountryListAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvFavCountries.setLayoutManager(layoutManager);
        binding.rvFavCountries.setAdapter(adapter);
    }
    private void fetchFavCountriesList(){
        viewModel.getFavouriteCountries().observe(this, new Observer<ArrayList<Country>>() {
            @Override
            public void onChanged(ArrayList<Country> countries) {
                binding.progressBar.setVisibility(View.GONE);
                if(countries.size()>0) {
                    adapter.setResponse(countries);
                    adapter.notifyDataSetChanged();
                }else{
                    Snackbar snackbar = Snackbar
                            .make(binding.coordinatorLayout, getResources().getString(R.string.msg_empty), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected) {
            fetchFavCountriesList();
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(binding.coordinatorLayout, getResources().getString(R.string.msg_offline), Snackbar.LENGTH_LONG);
            snackbar.show();
            adapter.setResponse(null);
            adapter.notifyDataSetChanged();
        }
    }
}
