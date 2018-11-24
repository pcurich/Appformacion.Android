package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.decoder.SchedulerDetailListDeserializer;
import com.qubicgo.android.appformacion.data.decoder.SchedulerListDeserializer;
import com.qubicgo.android.appformacion.data.request.SchedulerDetailListRequest;
import com.qubicgo.android.appformacion.data.request.SchedulerListRequest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFactoryScheduler extends ServiceFactory {

    public static Retrofit getSchedulerListConfiguration(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(SchedulerListRequest.class, new SchedulerListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }

    public static Retrofit getSchedulerDetailListConfiguration(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(SchedulerDetailListRequest.class, new SchedulerDetailListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }
}
