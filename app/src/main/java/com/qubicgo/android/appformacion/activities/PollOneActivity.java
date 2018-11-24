package com.qubicgo.android.appformacion.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.custom.ButtonCustom;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.data.request.PollActiveRequest;
import com.qubicgo.android.appformacion.data.response.PollActiveResponse;
import com.qubicgo.android.appformacion.data.response.PollResponse;
import com.qubicgo.android.appformacion.services.def.IServicePoll;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryPoll;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollOneActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextViewBoldCustom tvTitle ;
    @BindView(R.id.tvAspect)
    TextViewCustom tvAspect;
    @BindView(R.id.tvNote)
    TextViewCustom tvNote;
    @BindView(R.id.tvQuestion)
    TextViewCustom tvQuestion;

    @BindView(R.id.tvEnd)
    TextViewBoldCustom tvEnd;

    @BindView(R.id.etSuggestion)
    EditText etSuggestion;

    Unbinder unbinder;

    private Context mContext;
    private ProgressDialog progressDialog;
    private PollActiveRequest.PollAspect model;
    private List<PollActiveRequest.PollAspect.PollQuestion> questions;


    static List<PollActiveResponse> pollActiveResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_one);

        mContext = PollOneActivity.this;

        questions = new ArrayList<>();
        pollActiveResponse = new ArrayList<>();

        model = (PollActiveRequest.PollAspect) getIntent().getSerializableExtra("active_poll_fragment_aspect");

        questions = model.getQuestions();

        unbinder = ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        String title = getIntent().getStringExtra("active_poll_fragment_name");
        tvTitle.setText(title);
        tvAspect.setText(model.getDescription());

        String note = questions.get(0).getNote();
        if(note!=null && note.length()>0){
            tvNote.setText(note);
        }else {
            tvNote.setHeight(0);
        }

        String question = questions.get(0).getQuestion();
        if(question!=null && question.length()>0){
            tvQuestion.setText(question);
        }else {
            tvQuestion.setHeight(0);
        }

    }

    @OnClick(R.id.tvEnd)
    public void onEndClicked(){



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_response_answer,null);
        ButtonCustom ok = view.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pollActiveResponse.clear();
                pollActiveResponse.add(new PollActiveResponse(
                        questions.get(0).getQuestionId(),
                        questions.get(0).getResponseList().get(0).getResponseId(),
                        etSuggestion.getText().toString()));

                Gson gson = new Gson();
                String json = gson.toJson(pollActiveResponse)
                        .replace("\\n","")
                        .replace("\\t","")
                        .replace("\\r","")
                        .replace("á","a")
                        .replace("é","e")
                        .replace("í","i")
                        .replace("ó","o")
                        .replace("ú","u")
                        .replace("Á","A")
                        .replace("É","E")
                        .replace("Í","I")
                        .replace("Ó","O")
                        .replace("Ú","U");


                Integer groupPersonId =  getIntent().getIntExtra("active_poll_fragment_groupPersonId",0);

                final IServicePoll pollService = ServiceFactoryPoll.save(mContext).create(IServicePoll.class);
                Call<PollResponse> response = pollService.obtenerRepuestaEncuestas(
                        mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                        groupPersonId,
                        model.getScheduleId(),
                        model.getTeacherId(),
                        json);

                response.enqueue(new Callback<PollResponse>() {
                    @Override
                    public void onResponse(Call<PollResponse> call, Response<PollResponse> response) {
                        if(response.body().isError()){
                            Toast.makeText(mContext,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        }
                        startActivity(new Intent(PollOneActivity.this,HomeActivity.class));
                    }

                    @Override
                    public void onFailure(Call<PollResponse> call, Throwable t) {
                        Toast.makeText(mContext,"Intentelo dentro de unos momentos",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        builder.setView(view);
        builder.create().show();

    }
}
