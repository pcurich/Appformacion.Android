package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.decoder.InvitationListDeserializer;
import com.qubicgo.android.appformacion.data.decoder.InvitationSchedulerListDeserializer;
import com.qubicgo.android.appformacion.data.decoder.InvitationTypeListDeserializer;
import com.qubicgo.android.appformacion.data.request.InvitationListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationSchedulerListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationTypeListRequest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFactoryInvitation extends ServiceFactory {

    public static Retrofit getListConfiguration(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(InvitationListRequest.class, new InvitationListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }

    public static Retrofit getSchedulerConfiguration(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(InvitationSchedulerListRequest.class, new InvitationSchedulerListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }

    public static Retrofit getListTypeResponseConfiguration(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(InvitationTypeListRequest.class, new InvitationTypeListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();
    }

    public static Retrofit setResponse(Context mContext) {

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(InvitationTypeListRequest.class, new InvitationTypeListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();
    }
}
