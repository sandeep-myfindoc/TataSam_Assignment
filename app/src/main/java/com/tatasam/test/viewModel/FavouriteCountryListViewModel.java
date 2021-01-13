package com.tatasam.test.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.tatasam.test.model.Country;
import com.tatasam.test.repositories.CountryListRepositry;

import java.util.ArrayList;

public class FavouriteCountryListViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Country>> favCountryListMutableLiveData;
    private CountryListRepositry mRepo;
    public void init(){
        if(favCountryListMutableLiveData!=null)
            return;
        mRepo = CountryListRepositry.getInstance();
        favCountryListMutableLiveData = mRepo.getFavouriteCountries();
    }
    public LiveData<ArrayList<Country>> getFavouriteCountries(){
        return favCountryListMutableLiveData;
    }
}
