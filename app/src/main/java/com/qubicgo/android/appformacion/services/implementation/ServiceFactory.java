package com.qubicgo.android.appformacion.services.implementation;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qubicgo.android.appformacion.data.PersistentCookieStore;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public  class ServiceFactory {

    public static HttpLoggingInterceptor logger = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    public static OkHttpClient getUnsafeOkHttpClient(final Context mContext) {

        try{
            //create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null,trustAllCerts, new SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            /*final SSLSocketFactory sslSocketFactory = new TLSSocketFactory().internalSSLSocketFactory;*/

            ProviderInstaller.installIfNeeded(mContext.getApplicationContext());

            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor addHeaders = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request.Builder request = chain.request().newBuilder();
                    /*
                    SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE);
                    String cookie1 = preferences.getString("Set-Cookie1", "");
                    String cookie2 = preferences.getString("Set-Cookie2", "");

                    if(cookie1!=""){
                        request.addHeader("Set-Cookie",cookie1);
                    }
                    if(cookie2!=""){
                        request.addHeader("Set-Cookie",cookie2);
                    }*/

                    return chain.proceed(request.build());
                }};

            //CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(mContext));
            CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(mContext), CookiePolicy.ACCEPT_ALL);

            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(logger)
                    .sslSocketFactory(sslSocketFactory)
                    .addInterceptor(interceptor)
                    .addInterceptor(addHeaders)
                    .cookieJar(new JavaNetCookieJar(cookieHandler))
                    // turn off timeouts (github.com/socketio/engine.io-client-java/issues/32)
                    .connectTimeout(0, TimeUnit.MILLISECONDS)
                    .readTimeout(0, TimeUnit.MILLISECONDS)
                    .writeTimeout(0, TimeUnit.MILLISECONDS)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    }).build();

            return okHttpClient;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
