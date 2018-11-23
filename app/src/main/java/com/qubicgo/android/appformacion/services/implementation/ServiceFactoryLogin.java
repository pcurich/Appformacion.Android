package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.request.KeepAlive;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFactoryLogin extends ServiceFactory {

    public static Retrofit getConfiguration(Context mContext){
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }

    public static Retrofit keepAlive(Context mContext){
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .enableComplexMapKeySerialization()

                .create();

        return new Retrofit.Builder()
                .baseUrl(mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppUrl",""))
                .addConverterFactory(new ToStringConverterFactory())
                //.addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient(mContext))
                .build();

    }

    static class ToStringConverterFactory extends Converter.Factory {
        private static   final MediaType MEDIA_TYPE = MediaType.parse("text/html");

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (KeepAlive.class.equals(type)) {

                return new Converter<ResponseBody, KeepAlive>() {
                    @Override
                    public KeepAlive convert(ResponseBody value)
                    {
                        try{
                            String data = value.string();

                            KeepAlive k = new KeepAlive(data);
                            return k;
                        }catch (Exception e){
                            return null;
                        }
                    }
                };
            }
            return null;
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                              Annotation[] methodAnnotations, Retrofit retrofit) {

            if (KeepAlive.class.equals(type)) {
                return new Converter<String, RequestBody>() {
                    @Override
                    public RequestBody convert(String value) throws IOException {
                        return RequestBody.create(MEDIA_TYPE, value);
                    }
                };
            }
            return null;
        }
    }

}
