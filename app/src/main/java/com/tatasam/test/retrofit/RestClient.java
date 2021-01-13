package com.tatasam.test.retrofit;


import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import androidx.annotation.NonNull;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Rest client
 */
public class RestClient {
    private static Retrofit retrofit = null;
    private final NetworkConnection networkConnection;
    public RestClient(NetworkConnection networkConnection) {
        this.networkConnection = networkConnection;
    }

    public  ApiService getClient(){
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(this.networkConnection.getBaseUrl())//https://api.github.com/
                    .client(getOkHttpClient())// setting regarding connection and handle failure cases
                    .addConverterFactory(GsonConverterFactory.create(gson)) // used to conver json/xml to pojo class.
                    .build();
        }
        ApiService api = retrofit.create(ApiService.class);
        return api;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder httpClient=null;
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    })
                    .callTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);
            //.addInterceptor(getInterceptor());// Basic Authantication
            return httpClient.build();
        }catch (Exception e){
            new RuntimeException();
        }
        return httpClient.build();
    }
    public static Interceptor getInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();//header("Authorization", Credentials.basic("aUsername","aPassword"))
                /*builder.addHeader("apiKey", BuildConfig.api_key);
                builder.addHeader("deviceType", BuildConfig.device_type);
                builder.addHeader("apiVersion", BuildConfig.api_version);*/
                //builder
                Request requestWithBasicAuth = builder.build();
                Response response = chain.proceed(requestWithBasicAuth);
                switch (response.code()){
                    case 404:
                        break;
                    case 500:
                        break;
                    default:
                        break;
                }
                return response;
            }
        };
    }
    public static Retrofit getRetrofit(){
        return retrofit;
    }
}
