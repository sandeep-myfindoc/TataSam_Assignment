package com.tatasam.test.UI.countryList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tatasam.test.R;
import com.tatasam.test.UI.SuperActivity;
import com.tatasam.test.UI.TataSamApplication;
import com.tatasam.test.UI.favouriteCountryList.FavouriteCountriesListActivity;
import com.tatasam.test.broadcastreceiver.ConnectivityReceiver;
import com.tatasam.test.databinding.ActivityMainBinding;
import com.tatasam.test.model.Country;
import com.tatasam.test.sharedpreferences.Prefs;
import com.tatasam.test.sharedpreferences.SharedPreferencesName;
import com.tatasam.test.util.CommonUtility;
import com.tatasam.test.viewModel.CountryListViewModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CountryListActivity extends SuperActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ActivityMainBinding binding;
    private CountryListViewModel viewModel;
    private CountryListAdapter adapter;
    private ArrayList<Country> countryList;
    private ConnectivityReceiver receiver;
    // variable declared to acheive pagination
    private int page = 1;
    private int limit = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(CountryListViewModel.class);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
        updateToolbarWithoutBackButton(getResources().getString(R.string.title_country_list));
        receiver = new ConnectivityReceiver();
        initRecyclerView();
        TataSamApplication.getInstance().setConnectivityListener(this);//assign refence of current activity to listener of Connectivity receiver
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (binding.scrollView != null) {
                    if (binding.scrollView.getChildAt(0).getBottom() <= (binding.scrollView.getHeight() + binding.scrollView.getScrollY())) {
                        //scroll view is at bottom
                        page = page + 1;
                        limit = limit+1;
                        Log.d("inside","onScrollChange"+page);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        viewModel.init(page, limit);
                        Log.d("Scroll","view at bottom");
                    }
                }
            }
        });;
        if(new CommonUtility(this).checkInternetConnection()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.init(page,limit);
            fetchCountriesList();
        }
        else{
            binding.progressBar.setVisibility(View.GONE);
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
        adapter = new CountryListAdapter(this, new ListListener() {
            @Override
            public void onLikeCountry(int pos, boolean isLike) {
                Log.d("pos: "+pos,"isLike: "+isLike);
                countryList.get(pos).setLike(!isLike);
                adapter.setResponse(countryList);
                adapter.notifyDataSetChanged();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvCountries.setLayoutManager(layoutManager);
        binding.rvCountries.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuFavorite:
                ArrayList<Country> favCountries = new ArrayList<>();
                for(Country temp : countryList) {
                    if (temp.isLike())
                        favCountries.add(temp);
                }
                String jsonFavCountries = new Gson().toJson(favCountries);
                Prefs.with(this).save(SharedPreferencesName.FAVOURITE_COUNTRIES,jsonFavCountries);// favourite countries stored in refence to show fav countries on FavCountryListActivity
                startActivity(new Intent(CountryListActivity.this, FavouriteCountriesListActivity.class));
                break;
        }
        return true;
    }
    //add observer
    private void fetchCountriesList(){
        viewModel.getCountries().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject countryListResponse) {
                countryList  = new ArrayList<>();
                Log.d("fetchCountriesList","fetchCountriesList");
                binding.progressBar.setVisibility(View.GONE);
                String json=countryListResponse.get("data").toString(); // place your json format here in double Quotes with proper escapes .......
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(json.trim());
                    Iterator<?> keys = jObject.keys();
                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        if ( jObject.get(key) instanceof JSONObject ) {
                            JSONObject ref = (JSONObject)jObject.get(key);
                            countryList.add(new Country(key,ref.getString("country"),ref.getString("region")));
                        }
                    }
                    adapter.setResponse(countryList);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected) {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.init(page++,limit);
            fetchCountriesList();
        }
        else
        {
            binding.progressBar.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(binding.coordinatorLayout, getResources().getString(R.string.msg_offline), Snackbar.LENGTH_LONG);
            snackbar.show();
            adapter.setResponse(null);
            adapter.notifyDataSetChanged();
        }
    }
    interface ListListener
    {
        public void onLikeCountry(int pos,boolean isLike);
    }
}
