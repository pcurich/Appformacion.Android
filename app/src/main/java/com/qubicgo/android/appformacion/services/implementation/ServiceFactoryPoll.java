package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.decoder.PollActiveListDeserializer;
import com.qubicgo.android.appformacion.data.decoder.PollAnswerSettingDeserializer;
import com.qubicgo.android.appformacion.data.decoder.PollResponseDeserializer;
import com.qubicgo.android.appformacion.data.request.PollActiveListRequest;
import com.qubicgo.android.appformacion.data.request.PollAnswerSettingRequest;
import com.qubicgo.android.appformacion.data.response.PollResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFactoryPoll extends ServiceFactory  {

    public static Retrofit getListPolls(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(PollActiveListRequest.class, new PollActiveListDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }

    public static Retrofit getListPollAnswer(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(PollAnswerSettingRequest.class, new PollAnswerSettingDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }

    public static Retrofit save(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(PollResponse.class, new PollResponseDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }
}
