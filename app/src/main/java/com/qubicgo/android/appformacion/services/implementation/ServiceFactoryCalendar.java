package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.decoder.CalendarListDeserializer;
import com.qubicgo.android.appformacion.data.request.CalendarListRequest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFactoryCalendar extends ServiceFactory{

    public static Retrofit getCalendarList(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(CalendarListRequest.class, new CalendarListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();
    }
}
