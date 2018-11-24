package com.qubicgo.android.appformacion.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.data.request.KeepAlive;
import com.qubicgo.android.appformacion.services.def.IServiceLogin;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryLogin;
import com.qubicgo.android.appformacion.utilities.CustomHelper;
import com.qubicgo.android.appformacion.utilities.FileManager;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity   {
    private final String TAG = getClass().getSimpleName();
    private static final long SPLASH_SCREEN_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                String AppUrl = CustomHelper.getMetaDataString(getApplicationContext(),"app_url");
                String AppJunction =  CustomHelper.getMetaDataString(getApplicationContext(),"app_junction");
                Boolean AppUseLogin =  CustomHelper.getMetaDataBoolean(getApplicationContext(),"app_use_login");
                final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                SharedPreferences.Editor  preferences = getSharedPreferences("formacionbbva", MODE_PRIVATE).edit();
                preferences.clear();

                preferences.putString("onTokenRefresh", refreshedToken);
                preferences.putString("AppUrl", AppUrl);
                preferences.putString("AppJunction", AppJunction);
                preferences.putBoolean("AppUseLogin", AppUseLogin);

                preferences.apply();

                final IServiceLogin loginService  = ServiceFactoryLogin.keepAlive(getApplicationContext()).create(IServiceLogin.class);
                final Call<KeepAlive> response = loginService.pinMessage(getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction", ""));

                response.enqueue(new Callback<KeepAlive>() {
                    @Override
                    public void onResponse(Call<KeepAlive> call, Response<KeepAlive> rawResponse) {
                        KeepAlive result = rawResponse.body();
                        if(result != null && result.isStatus()){
                            String registro = FileManager.readFromFile(getApplicationContext());
                            if (!"".equals(registro)){
                                SharedPreferences.Editor  editor = getSharedPreferences("formacionbbva", MODE_PRIVATE).edit();
                                editor.putString("username", registro);
                                editor.apply();

                                Intent intent  = new Intent(SplashActivity.this, HomeActivity.class);
                                Toast.makeText(SplashActivity .this, "Sesion activa", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();

                            }else {
                                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                                Toast.makeText(SplashActivity.this, "Sesion cerrada por inactividad", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Intent intent   = new Intent(SplashActivity.this, SignInActivity.class);
                            Toast.makeText(SplashActivity .this, " Sesion cerrada por inactividad", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<KeepAlive> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(SplashActivity .this, "Al login", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(SplashActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }



}

