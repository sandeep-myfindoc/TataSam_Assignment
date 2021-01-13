package com.tatasam.test.viewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.JsonObject;
import com.tatasam.test.repositories.CountryListRepositry;
//JsonObject
public class CountryListViewModel extends ViewModel {
    private MutableLiveData<JsonObject> countryListResponseMutableLiveData;
    private CountryListRepositry mRepo;
    public void init(int page,int limit){
        mRepo = CountryListRepositry.getInstance();
        countryListResponseMutableLiveData = mRepo.getCountries(page,limit);
    }
    public LiveData<JsonObject> getCountries(){
        return countryListResponseMutableLiveData;
    }
}
