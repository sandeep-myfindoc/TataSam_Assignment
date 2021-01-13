package com.tatasam.test.repositories;
import android.app.Activity;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tatasam.test.UI.TataSamApplication;
import com.tatasam.test.model.Country;
import com.tatasam.test.retrofit.ApiService;
import com.tatasam.test.retrofit.NetworkConnection;
import com.tatasam.test.retrofit.RestClient;
import com.tatasam.test.sharedpreferences.Prefs;
import com.tatasam.test.sharedpreferences.SharedPreferencesName;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryListRepositry {
    private static CountryListRepositry instance;
    MutableLiveData<JsonObject> data= new MutableLiveData<JsonObject>();
    public static CountryListRepositry getInstance(){
        if(instance==null){
            instance = new CountryListRepositry();
        }
        return instance;
    }

    public MutableLiveData<JsonObject> getCountries(final int page, int limit){

        ApiService api = new RestClient(new NetworkConnection()).getClient();
        api.STRING_CALL(page,limit).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("response","recived"+page);
                if(response!=null){
                    Log.d("response",response.body()+"");
                    data.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("onFailure",t.getMessage()+"");
            }
        });
        return data;
    }


    public MutableLiveData<ArrayList<Country>> getFavouriteCountries(){
        final MutableLiveData<ArrayList<Country>> data = new MutableLiveData<ArrayList<Country>>();
        data.setValue(setData());
        return data;
    }

    private ArrayList<Country> setData() {
        String json =  Prefs.with(TataSamApplication.getInstance()).getString(SharedPreferencesName.FAVOURITE_COUNTRIES,null);
        Type type = new TypeToken<ArrayList<Country>>() {}.getType();
        return new Gson().fromJson(json,type);
    }
}
