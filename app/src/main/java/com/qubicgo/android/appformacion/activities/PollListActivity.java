package com.qubicgo.android.appformacion.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.custom.ButtonCustom;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.custom.TextViewJustifiedCustom;
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

public class PollListActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextViewBoldCustom tvTitle;
    @BindView(R.id.tvAspect)
    TextViewCustom tvAspect;
    @BindView(R.id.tvTeacher)
    TextViewCustom tvTeacher;
    @BindView(R.id.tvNote)
    TextViewCustom tvNote;
    @BindView(R.id.tvQuestion)
    TextViewJustifiedCustom  tvQuestion;
    @BindView(R.id.tvPrevious)
    TextViewBoldCustom tvPrevious;
    @BindView(R.id.tvNext)
    TextViewBoldCustom tvNext;

    @BindView(R.id.recyclerAlternative)
    RecyclerView recyclerAlternative;

    private Context mContext;
    private ProgressDialog progressDialog;
    private PollAnswerListAdapter adapter;

    private PollActiveRequest.PollAspect aspect;
    private PollActiveRequest.PollAspect.PollQuestion currentQuestion;
    private Integer indexQuestion = -1;

    static List<PollActiveResponse> pollActiveResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_list);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.text_active_polls_large);
        mContext = this;

        pollActiveResponse = new ArrayList<>();
        aspect = (PollActiveRequest.PollAspect) getIntent().getSerializableExtra("active_poll_fragment_aspect");

        String title = getIntent().getStringExtra("active_poll_fragment_name");
        tvTitle.setText(title);
        tvAspect.setText(aspect.getDescription());

        if (aspect.getTeacherName() != null && aspect.getTeacherName().length() > 0) {
            tvTeacher.setText(aspect.getTeacherName());
        } else {
            tvTeacher.setHeight(0);
        }

        stepQuestion(1);
        cleanTitle();

        adapter = new PollAnswerListAdapter(mContext, currentQuestion.getResponseList(),
                new PollListActivity.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, ViewModel viewModel) { adapter.notifyDataSetChanged(); }
                });

        int spanCount = getSpanCount(currentQuestion.getResponseList());
        recyclerAlternative.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        recyclerAlternative.setAdapter(adapter);
        recyclerAlternative.setHasFixedSize(true);

        reloadRecycler();
    }

    private void stepQuestion(int i) {

        if (indexQuestion + i < 0) {
            return;
        } else {
            indexQuestion = indexQuestion + i;
            currentQuestion = aspect.getQuestions().get(indexQuestion);

            tvQuestion.setText(currentQuestion.getQuestion());
            String note = currentQuestion.getNote();
            if (note != null && note.length() > 0) {
                tvNote.setText(note);
            } else {
                tvNote.setHeight(0);
            }
        }

    }

    private void cleanTitle() {
        tvNext.setText("Siguiente");
        tvPrevious.setText("Anterior");

        if (indexQuestion == aspect.getQuestions().size() - 1) {
            //el ultimo
            tvNext.setText("Finalizar");
        }
        if (indexQuestion <= 0) {
            tvPrevious.setText("");
        }
    }

    private void reloadRecycler() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (adapter != null) {
            for (PollActiveResponse response : pollActiveResponse) {
                if (currentQuestion.getQuestionId() == response.getPreguntaId()) {
                    for (PollActiveRequest.PollAspect.PollQuestion.PollAnswer a : currentQuestion.getResponseList()) {
                        if (a.getResponseId() == response.getRtaId()) {
                            a.setSelected(true);
                        } else {
                            a.setSelected(false);
                        }
                    }
                }
            }

            adapter.setList(currentQuestion.getResponseList());
            adapter.notifyDataSetChanged();
        }

        progressDialog.dismiss();
    }

    @OnClick(R.id.tvPrevious)
    public void onPreviousClicked(){
        stepQuestion(-1);
        cleanTitle();
        reloadRecycler();
    }

    @OnClick(R.id.tvNext)
    public void onNextClicked(){

        if(isSelectedAnswer()){

            if(indexQuestion == aspect.getQuestions().size()-1){
                // se va a enviar la respuestas

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.dialog_response_answer,null);
                ButtonCustom ok = view.findViewById(R.id.btnOk);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Gson gson = new Gson();
                        String json = gson.toJson(pollActiveResponse);
                        Integer groupPersonId =  getIntent().getIntExtra("active_poll_fragment_groupPersonId",0);
                        final IServicePoll pollService = ServiceFactoryPoll.save(mContext).create(IServicePoll.class);
                        Call<PollResponse> response = pollService.obtenerRepuestaEncuestas(
                                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                                groupPersonId,
                                aspect.getScheduleId(),
                                aspect.getTeacherId(),
                                json);

                        response.enqueue(new Callback<PollResponse>() {
                            @Override
                            public void onResponse(Call<PollResponse> call, Response<PollResponse> response) {
                                if(response.body().isError()){
                                    Toast.makeText(mContext,response.body().getMessage(),Toast.LENGTH_LONG).show();
                                }
                                startActivity(new Intent(PollListActivity.this,HomeActivity.class));
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
            }else{
                stepQuestion(1);
                cleanTitle();
                reloadRecycler();
            }
        }else{
            Toast.makeText(mContext,"No ha seleccionado una alternativa",Toast.LENGTH_LONG).show();
        }
    }

    private boolean isSelectedAnswer() {

        for (PollActiveRequest.PollAspect.PollQuestion.PollAnswer response : currentQuestion.getResponseList()) {
            if (response.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private int getSpanCount(List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer> responseList) {
        int maxLength = 0;
        for (PollActiveRequest.PollAspect.PollQuestion.PollAnswer i : responseList) {
            if (i.getValue().length() > maxLength) {
                maxLength = i.getValue().length();
            }
        }

        if (maxLength >= 1 && 3 > maxLength)
            return 5;
        if (maxLength >= 3 && 10 > maxLength)
            return 3;
        if (maxLength >= 10 && 20 > maxLength)
            return 2;
        if (maxLength >= 20)
            return 1;

        return 1;
    }

    static class PollAnswerListAdapter extends RecyclerView.Adapter<PollAnswerListAdapter.PollAnswerViewHolder> {

        private Context mContext;
        private List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer> list;
        private OnItemClickListener itemClickListener;

        public void setList(List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer> list) {
            this.list = list;
        }

        public PollAnswerListAdapter(Context mContext,
                                     List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer> list,
                                     OnItemClickListener listener) {
            this.mContext = mContext;
            this.list = list;
            itemClickListener = listener;
        }

        @NonNull
        @Override
        public PollAnswerListAdapter.PollAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_poll_answer, parent, false);
            return new PollAnswerListAdapter.PollAnswerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PollAnswerViewHolder holder, int position) {
            holder.bind(list.get(position), position, itemClickListener);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class PollAnswerViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tvAlternative)
            TextViewBoldCustom tvAlternative;

            @BindView(R.id.flBackground)
            FrameLayout flBackground;


            PollAnswerViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(PollActiveRequest.PollAspect.PollQuestion.PollAnswer aspect, final int position, final OnItemClickListener listener) {
                tvAlternative.setText(aspect.getValue());

                if (!list.get(position).isSelected()) {
                    flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_pair));
                } else {
                    flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_no_pair));
                }

                flBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        for (PollActiveRequest.PollAspect.PollQuestion.PollAnswer i : list) {
                            i.setSelected(false);
                        }

                        list.get(getAdapterPosition()).setSelected(true);
                        flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_no_pair));

                        boolean updated = false;
                        for (PollActiveResponse response : pollActiveResponse) {
                            //si ya estaba seleccionado
                            if (list.get(getAdapterPosition()).getQuestionId().equals(response.getPreguntaId())) {
                                //actualizo nomas
                                response.setRtaId(list.get(getAdapterPosition()).getResponseId());
                                updated = true;
                                break;
                            }
                        }
                        if (!updated) {

                            pollActiveResponse.add(new PollActiveResponse(
                                    list.get(getAdapterPosition()).getQuestionId(),
                                    list.get(getAdapterPosition()).getResponseId(),
                                    list.get(getAdapterPosition()).getName()));
                        }

                        listener.onItemClick(null, null);
                    }
                });

            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, ViewModel viewModel);
    }
}
