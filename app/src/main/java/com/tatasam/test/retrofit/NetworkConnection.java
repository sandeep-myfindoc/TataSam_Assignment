package com.tatasam.test.retrofit;
import com.tatasam.test.BuildConfig;
public class NetworkConnection {
    private String baseUrl;
    public NetworkConnection() {
        if(BuildConfig.DEBUG){
            this.baseUrl = BuildConfig.baseUrl;
        }else{
            this.baseUrl = BuildConfig.baseUrl;
        }
    }
    public String getBaseUrl() {
        return baseUrl;
    }
}
