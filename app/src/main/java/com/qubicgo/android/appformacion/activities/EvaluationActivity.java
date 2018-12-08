package com.qubicgo.android.appformacion.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.custom.ButtonCustom;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewJustifiedCustom;
import com.qubicgo.android.appformacion.data.request.EvaluationActiveRequest;
import com.qubicgo.android.appformacion.data.response.EvaluationActiveResponse;
import com.qubicgo.android.appformacion.data.response.EvaluationResponse;
import com.qubicgo.android.appformacion.services.def.IServiceEvaluation;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryEvaluation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationActivity extends AppCompatActivity {

    @BindView(R.id.tvQuestion)
    TextViewJustifiedCustom tvQuestion;

    @BindView(R.id.tvPrevious)
    TextViewBoldCustom tvPrevious;
    @BindView(R.id.tvNext)
    TextViewBoldCustom tvNext;

    @BindView(R.id.recyclerAlternative)
    RecyclerView recyclerAlternative;

    private Context mContext;
    private EvaluationAdapter adapter;

    private EvaluationActiveRequest.EvaluationRequest aspect;
    private EvaluationActiveRequest.EvaluationRequest.QuestionRequest currentQuestion;
    private Integer indexQuestion = -1;
    private Map<Integer, List<Integer>> respponseIds;

    static List<EvaluationActiveResponse> evaluationActiveResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.text_active_evaluations_large);
        mContext = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        evaluationActiveResponse = new ArrayList<>();
        respponseIds = new HashMap<>();

        aspect = (EvaluationActiveRequest.EvaluationRequest) getIntent().getSerializableExtra("active_evaluation_fragment_aspect");

        stepQuestion(1);
        cleanTitle();

    }

    private void stepQuestion(int i) {

        if (indexQuestion + i < 0) {
            return;
        } else {
            indexQuestion = indexQuestion + i;
            currentQuestion = aspect.getQuestions().get(indexQuestion);

            tvQuestion.setText(currentQuestion.getQuestion());
            if (!respponseIds.containsKey(currentQuestion.getQuestionId()))
                respponseIds.put(currentQuestion.getQuestionId(), new ArrayList<Integer>());

            Boolean isUnique = "OPC_SIM".equals(currentQuestion.getCodeQuestion());
            adapter = new EvaluationAdapter(mContext, isUnique, currentQuestion.getAlternatives(),
                    new EvaluationActivity.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, ViewModel viewModel) {
                            adapter.notifyDataSetChanged();
                        }
                    });

            recyclerAlternative.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            recyclerAlternative.setAdapter(adapter);
            recyclerAlternative.setHasFixedSize(true);

            reloadRecycler();
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
        if (adapter != null) {
            adapter.setList(currentQuestion.getAlternatives());
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.tvPrevious)
    public void onPreviousClicked() {

        stepQuestion(-1);
        cleanTitle();
        reloadRecycler();

    }

    @OnClick(R.id.tvNext)
    public void onNextClicked() {


        if (respponseIds.get(currentQuestion.getQuestionId()).size() > 0) {

            if (indexQuestion == aspect.getQuestions().size() - 1) {
                // se va a enviar la respuestas

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.dialog_response_answer, null);
                ButtonCustom ok = view.findViewById(R.id.btnOk);
                builder.setView(view);
                builder.create();
                final AlertDialog show = builder.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Gson gson = new Gson();
                        CreateResponse();
                        final String json = gson.toJson(evaluationActiveResponse);

                        IServiceEvaluation evaluationService = ServiceFactoryEvaluation.save(mContext).create(IServiceEvaluation.class);
                        Call<EvaluationResponse> response = evaluationService.recepcionRespuestaEvaluacion(
                                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction", ""),
                                aspect.getGrppId(), json);

                        response.enqueue(new Callback<EvaluationResponse>() {
                            @Override
                            public void onResponse(Call<EvaluationResponse> call, Response<EvaluationResponse> response) {

                                final Intent intent = new Intent(mContext, ScoreActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                intent.putExtra("result_evaluation_porcentage" ,aspect.getPercentage());
                                intent.putExtra("result_evaluation_good", (Serializable)getGoodAnswer());
                                intent.putExtra("result_evaluation_bad",(Serializable)getBadAnswer());

                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<EvaluationResponse> call, Throwable t) {
                                Toast.makeText(mContext, "Intentelo dentro de unos momentos", Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor = getSharedPreferences("formacionbbva", MODE_PRIVATE).edit();
                                editor.putString("jsonEvaluation", json);
                                editor.putInt("getGrppId", aspect.getGrppId());
                                editor.apply();
                            }
                        });
                        show.dismiss();
                    }
                });
            } else {
                stepQuestion(1);
                cleanTitle();
                reloadRecycler();
            }
        } else {
            Toast.makeText(mContext, "No ha seleccionado una alternativa", Toast.LENGTH_LONG).show();
        }


    }

    private List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest> getBadAnswer() {

        List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest> bad = new ArrayList<>();

        for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest q : aspect.getQuestions()) {
            List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> alternative = new ArrayList<>();

            if ("OPC_SIM".equals(q.getCodeQuestion())) {
                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest r : q.getAlternatives()) {
                    if ("RPTA_COR".equals(r.getCodeAlternative()) && !r.isSelected()) {
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (Correcto)", r.getCodeAlternative()));
                    }
                    if ("RPTA_INC".equals(r.getCodeAlternative()) && r.isSelected()) {
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (Error)", r.getCodeAlternative()));
                    }
                }
                if (alternative.size() > 0) {
                    bad.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest(q.getQuestionId(), q.getOrden(), q.getQuestion(), q.getCodeQuestion(), alternative));
                    alternative.clear();
                    alternative = new ArrayList<>();
                }
            } else {
                List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> temp = getCorrectAnswer(q);
                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest r : temp) {
                    if ("RPTA_COR".equals(r.getCodeAlternative()) && r.isSelected()) {
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (Seleccionado)", r.getCodeAlternative()));
                    } else if ("RPTA_COR".equals(r.getCodeAlternative()) && r.isSelected()) {
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (No Seleccionado)", r.getCodeAlternative()));
                    }
                    else{
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (No Seleccionado)", "RPTA_INC"));
                    }
                }
                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest r : q.getAlternatives()) {
                    if ("RPTA_INC".equals(r.getCodeAlternative()) && r.isSelected()) {
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (Error)", r.getCodeAlternative()));
                    }
                }
                if(temp.size() <= alternative.size()) {
                    bad.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest(q.getQuestionId(), q.getOrden(), q.getQuestion(), q.getCodeQuestion(), alternative));
                }
            }

        }
        return bad;
    }

    private List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest> getGoodAnswer() {

        List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest> good = new ArrayList<>();
        List<Integer> listToMarkToDelete = new ArrayList<>();

        for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest q : aspect.getQuestions()) {
            List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> alternative = new ArrayList<>();

            if ("OPC_SIM".equals(q.getCodeQuestion())) {
                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest r : q.getAlternatives()) {
                    if ("RPTA_COR".equals(r.getCodeAlternative()) && r.isSelected()) {
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (Correcto)", r.getCodeAlternative()));
                        good.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest(q.getQuestionId(),q.getOrden(),q.getQuestion(),q.getCodeQuestion(),alternative));
                        listToMarkToDelete.add(q.getQuestionId());
                    }
                }
            } else {
                List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> temp = getCorrectAnswer(q);

                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest r : temp) {
                    if ("RPTA_COR".equals(r.getCodeAlternative()) && r.isSelected()) {
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (Correcto)", r.getCodeAlternative()));
                    }
                }

                if (alternative.size()>1){

                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest r : q.getAlternatives()) {
                    if ("RPTA_INC".equals(r.getCodeAlternative()) && r.isSelected()) {
                        alternative.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(r.getAlternativeId(), r.getDescription() + " (Correcto)", r.getCodeAlternative()));
                    }
                }
            }

                if(temp.size() == alternative.size()) {
                    listToMarkToDelete.add(q.getQuestionId());
                    good.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest(q.getQuestionId(), q.getOrden(), q.getQuestion(), q.getCodeQuestion(), alternative));
                }
            }
            alternative = new ArrayList<>();
        }

        for (Integer i : listToMarkToDelete) {
            EvaluationActiveRequest.EvaluationRequest.QuestionRequest  toDelete = returnQuestion(i);
            if(toDelete!= null){
                aspect.getQuestions().remove(toDelete);
            }
        }

        return good;
    }

    private EvaluationActiveRequest.EvaluationRequest.QuestionRequest  returnQuestion(int id){
        for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest q : aspect.getQuestions()){
            if(q.getQuestionId() == id){
                return q;
            }
        }
        return null;
    }


    private List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest>  getCorrectAnswer(EvaluationActiveRequest.EvaluationRequest.QuestionRequest q) {
        List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest>  alternative = new ArrayList<>();

        for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest r :  q.getAlternatives()){
            if ("RPTA_COR".equals( r.getCodeAlternative())){
                alternative.add(r);
            }
        }
        return alternative;
    }

    private void CreateResponse() {
        boolean wasOk = true;

        for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest q : aspect.getQuestions()) {
            for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest a : q.getAlternatives()) {
                if (a.isSelected()) {
                    wasOk = wasOk && "RPTA_COR".equals(a.getCodeAlternative());
                }
            }
            evaluationActiveResponse.add(new EvaluationActiveResponse(
                    q.getQuestionId(),
                    q.getCodeQuestion(),
                    respponseIds.get(q.getQuestionId()),
                    wasOk));
        }
    }

    class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.EvaluationViewHolder> {

        private Context mContext;
        private Boolean isUnique;
        private List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> list;
        private OnItemClickListener itemClickListener;

        public void setList(List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> list) {
            this.list = list;
        }

        public EvaluationAdapter(Context mContext, Boolean isUnique,
                                 List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> list,
                                 OnItemClickListener listener) {
            this.mContext = mContext;
            this.list = list;
            this.isUnique = isUnique;
            itemClickListener = listener;
        }

        @NonNull
        @Override
        public EvaluationAdapter.EvaluationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_evaluation_answer, parent, false);
            return new EvaluationAdapter.EvaluationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EvaluationViewHolder holder, int position) {
            if (isUnique) {
                holder.bindUnique(list.get(position), itemClickListener);
            } else {
                holder.bindNoUnique(list.get(position), itemClickListener);
            }
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class EvaluationViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.ivPicture)
            ImageView picture;

            @BindView(R.id.llContainer)
            LinearLayout llContainer;

            @BindView(R.id.tvResponse)
            TextView tvResponse;

            EvaluationViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bindUnique(EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative,
                                   final OnItemClickListener listener) {

                if (list.get(getAdapterPosition()).isSelected()) {
                    picture.setBackground(mContext.getResources().getDrawable(R.drawable.check));
                }
                if (!list.get(getAdapterPosition()).isSelected()) {
                    picture.setBackground(mContext.getResources().getDrawable(R.drawable.uncheck));
                }

                tvResponse.setText(alternative.getDescription());

                llContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        respponseIds.get(currentQuestion.getQuestionId()).clear();
                        for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest r : list) {
                            r.setSelected(false);
                        }

                        list.get(getAdapterPosition()).setSelected(true);
                        List<Integer> rpt = respponseIds.get(currentQuestion.getQuestionId());
                        if (rpt == null)
                            rpt = new ArrayList<>();
                        rpt.add(list.get(getAdapterPosition()).getAlternativeId());

                        respponseIds.put(currentQuestion.getQuestionId(), rpt);

                        listener.onItemClick(null, null);
                    }
                });

            }

            public void bindNoUnique(EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative,
                                     final OnItemClickListener listener) {

                if (list.get(getAdapterPosition()).isSelected()) {
                    picture.setBackground(mContext.getResources().getDrawable(R.drawable.mark));
                }
                if (!list.get(getAdapterPosition()).isSelected()) {
                    picture.setBackground(mContext.getResources().getDrawable(R.drawable.unmark));
                }

                tvResponse.setText(alternative.getDescription());

                llContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (list.get(getAdapterPosition()).isSelected()) {
                            list.get(getAdapterPosition()).setSelected(false);
                            List<Integer> rpt = respponseIds.get(currentQuestion.getQuestionId());
                            rpt.remove(list.get(getAdapterPosition()).getAlternativeId());
                            respponseIds.put(currentQuestion.getQuestionId(), rpt);
                        } else {
                            list.get(getAdapterPosition()).setSelected(true);
                            List<Integer> rpt = respponseIds.get(currentQuestion.getQuestionId());
                            if (rpt == null)
                                rpt = new ArrayList<>();
                            rpt.add(list.get(getAdapterPosition()).getAlternativeId());
                            respponseIds.put(currentQuestion.getQuestionId(), rpt);
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
