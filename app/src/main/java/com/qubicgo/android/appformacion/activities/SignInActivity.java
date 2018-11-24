package com.qubicgo.android.appformacion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.custom.EditTextCustom;
import com.qubicgo.android.appformacion.data.request.EnrollDeviceRequest;
import com.qubicgo.android.appformacion.data.response.EvaluationResponse;
import com.qubicgo.android.appformacion.services.def.IServiceEvaluation;
import com.qubicgo.android.appformacion.services.def.IServiceLogin;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryEvaluation;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryLogin;
import com.qubicgo.android.appformacion.utilities.FileManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.etUsername)
    EditTextCustom etUsername;
    @BindView(R.id.etPassword)
    EditTextCustom etPassword;

    private Context mContext;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mContext = SignInActivity.this;
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSignIn)
    public void onViewClicked() {

        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getBoolean("AppUseLogin", false)) {

            SharedPreferences.Editor editor = getSharedPreferences("formacionbbva", MODE_PRIVATE).edit();
            editor.putString("username", "P014773");
            editor.putString("pwd", "");
            editor.apply();

            final IServiceLogin loginService = ServiceFactoryLogin.getConfiguration(mContext).create(IServiceLogin.class);
            enrollDevice(loginService, username);

            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);

        } else {

            final IServiceLogin loginService = ServiceFactoryLogin.getConfiguration(mContext).create(IServiceLogin.class);

            final String strRequestBody = "username=" + username + "&password=" + password + "&login-form-type=pwd";
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), strRequestBody);

            final Call<ResponseBody> loginResponse = loginService.login(requestBody);
            loginResponse.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> rawResponse) {
                    try {
                        String response = rawResponse.body().string();
                        String headers = rawResponse.headers().toString();

                        if (headers.contains("PD-S-SESSION-ID") || headers.contains("Ltpatoken2") ||
                                headers.contains("PD-ID") ||
                                headers.contains("x-mule_session") ||
                                response.contains("Your login was successful") ||
                                response.contains("Valid Administration Commands")) {

                            SharedPreferences.Editor editor = getSharedPreferences("formacionbbva", MODE_PRIVATE).edit();
                            editor.putString("username", username);
                            editor.putString("pwd", password);
                            editor.apply();

                            FileManager.writeToFile(username,getApplicationContext());

                            enrollDevice(loginService, username);
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SignInActivity.this, "Error de conexión con WebSeal", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    if(progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void enrollDevice(IServiceLogin loginService, String username) {

        //Enrolar dispositivo
        String android_id = Settings.Secure.getString(SignInActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        // Firebase token
        final SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva", Context.MODE_PRIVATE);
        String token = preferences.getString("onTokenRefresh", "");

        if ("" == token) {
            token = FirebaseInstanceId.getInstance().getToken();
        }

        Call<EnrollDeviceRequest> responseEnroll = loginService.enrollDevice(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction", ""),
                Build.ID, Build.BRAND, Build.MODEL, "ANDROID",
                String.valueOf(Build.VERSION.SDK_INT), token, username);

        responseEnroll.enqueue(new Callback<EnrollDeviceRequest>() {

            @Override
            public void onResponse(Call<EnrollDeviceRequest> call, Response<EnrollDeviceRequest> response) {

                //resultado del enrolamiento
                EnrollDeviceRequest result = response.body();
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }

                // Si es ok o error igual va para el dasboard
                String json = preferences.getString("jsonEvaluation", "");
                Integer getGrppId = preferences.getInt("getGrppId", 0);

                if (!"".equals(json) && getGrppId != 0) {
                    sendEvaluarions(getGrppId, json);
                }

                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<EnrollDeviceRequest> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SignInActivity.this, "Error con el servicio de enrolamiento", Toast.LENGTH_SHORT).show();
                Toast.makeText(SignInActivity.this,t.getCause().getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(SignInActivity.this,t.getCause().getStackTrace().toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(SignInActivity.this,t.toString(), Toast.LENGTH_LONG).show();
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }

                if (t.toString().contains("BEGIN_OBJECT")) {

                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void sendEvaluarions(Integer getGrppId, String json) {

        IServiceEvaluation evaluationService = ServiceFactoryEvaluation.save(mContext).create(IServiceEvaluation.class);
        Call<EvaluationResponse> responseEvaluation = evaluationService.recepcionRespuestaEvaluacion(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction", ""),
                getGrppId, json);

        responseEvaluation.enqueue(new Callback<EvaluationResponse>() {
            @Override
            public void onResponse(Call<EvaluationResponse> call, Response<EvaluationResponse> response) {
                if (!response.body().isError()) {
                    final SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva", Context.MODE_PRIVATE);
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    preferences.edit().remove("jsonEvaluation");
                    preferences.edit().remove("getGrppId");
                    preferences.edit().apply();
                }
            }

            @Override
            public void onFailure(Call<EvaluationResponse> call, Throwable t) {
                Toast.makeText(mContext, "Intentelo dentro de unos momentos", Toast.LENGTH_LONG).show();
            }
        });
    }
}
