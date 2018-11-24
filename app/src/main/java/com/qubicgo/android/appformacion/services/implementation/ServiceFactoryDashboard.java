package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.decoder.DashboardDeserializer;
import com.qubicgo.android.appformacion.data.request.DashboardRequest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFactoryDashboard extends ServiceFactory  {

    public static Retrofit getConfiguration(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(DashboardRequest.class, new DashboardDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                //prod "https://extranetperu.grupobbva.pe:10443/"
                //dev "https://extranetdev.grupobbva.pe:10443/"
                //.baseUrl("https://extranetperu.grupobbva.pe:10443/")
                //tjboss8485/appformacionbg/
                //.baseUrl("https://extranetdev.grupobbva.pe/appformacionbg/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(ServiceFactory.getUnsafeOkHttpClient(mContext))
                .build();

    }
}
