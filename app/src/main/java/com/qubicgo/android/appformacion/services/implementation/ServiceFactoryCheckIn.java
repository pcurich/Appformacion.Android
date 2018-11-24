package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.decoder.CheckInDeserializer;
import com.qubicgo.android.appformacion.data.decoder.CheckInDetailListDeserializer;
import com.qubicgo.android.appformacion.data.decoder.CheckInListDeserializer;
import com.qubicgo.android.appformacion.data.request.CheckIn;
import com.qubicgo.android.appformacion.data.request.CheckInDetailListRequest;
import com.qubicgo.android.appformacion.data.request.CheckInListRequest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFactoryCheckIn extends ServiceFactory{

    public static Retrofit getCheckInListConfiguration(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(CheckInListRequest.class, new CheckInListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();
    }

    public static Retrofit getCheckInDetailConfiguration(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(CheckInDetailListRequest.class, new CheckInDetailListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();
    }

    public static Retrofit checkIn(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(CheckIn.class, new CheckInDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();
    }
}
