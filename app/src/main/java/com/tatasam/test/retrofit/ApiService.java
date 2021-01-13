package com.tatasam.test.retrofit;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

//http://192.168.10.63:86
public interface ApiService {
    // Login
    @GET("data/v1/countries")
    Call<JsonObject>
STRING_CALL(@Query("page") int page,@Query("limit") int limit);     //Call<JsonObject> getListOfCountries();
}