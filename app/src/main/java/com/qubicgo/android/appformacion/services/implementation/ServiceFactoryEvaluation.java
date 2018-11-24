package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.decoder.EvaluationActiveListDeserializer;
import com.qubicgo.android.appformacion.data.decoder.EvaluationResponseDeserializer;
import com.qubicgo.android.appformacion.data.decoder.PollResponseDeserializer;
import com.qubicgo.android.appformacion.data.request.EvaluationActiveListRequest;
import com.qubicgo.android.appformacion.data.response.EvaluationResponse;
import com.qubicgo.android.appformacion.data.response.PollResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFactoryEvaluation extends ServiceFactory  {

    public static Retrofit getListEvaluations(Context mContext){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .registerTypeAdapter(EvaluationActiveListRequest.class, new EvaluationActiveListDeserializer())
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
                .registerTypeAdapter(EvaluationResponse.class, new EvaluationResponseDeserializer())
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }
}
