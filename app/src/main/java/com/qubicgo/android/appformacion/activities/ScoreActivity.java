package com.qubicgo.android.appformacion.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.data.request.EvaluationActiveRequest;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class ScoreActivity extends AppCompatActivity {

    @BindView(R.id.tvScore)
    TextViewBoldCustom tvScore;
    @BindView(R.id.tvRespuestas)
    TextViewCustom tvRespuestas;
    @BindView(R.id.tvNext)
    TextViewBoldCustom tvNext;
    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.rvResponse)
    RecyclerView rvResponse;

    private EvaluationActiveRequest.EvaluationRequest aspect;
    private List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest> filterQuestion;
    private Context mContext;
    SectionedRecyclerViewAdapter sectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.text_result);
        mContext = this;

        filterQuestion = new ArrayList<>();
        aspect = (EvaluationActiveRequest.EvaluationRequest) getIntent().getSerializableExtra("result_evaluation_aspect");
        sectionAdapter = new SectionedRecyclerViewAdapter();

        int good = getCorrect();
        int bad = aspect.getQuestions().size() - getCorrect();
        loadChart(bad, good);
        NumberFormat format = NumberFormat.getPercentInstance(Locale.US);
        String percentage = format.format((good * 1.0 / (bad + good)));
        tvScore.setText(percentage);
        tvRespuestas.setText(aspect.getPercentage() <= (good * 100 / (bad + good)) ? "Aprobado" : "Desaprobado");

    }

    private void loadChart(int incorrects, int corrects) {
        pieChart.setDescription(null);
        pieChart.getLegend().setEnabled(false);

        pieChart.setCenterTextSize(10f);
        pieChart.setHoleRadius(50f);

        pieChart.setOnChartValueSelectedListener(new GestureChart());

        ArrayList<PieEntry> entries = new ArrayList<>();
        PieEntry badEnry = new PieEntry(incorrects, "Malas");
        entries.add(badEnry);
        entries.add(new PieEntry(corrects, "Buenas"));

        PieDataSet dataset = new PieDataSet(entries, "Datos");

        List<Integer> listColor = new ArrayList<>();
        listColor.add(getResources().getColor(R.color.sky_blue));
        listColor.add(getResources().getColor(R.color.blue));
        dataset.setValueTextColors(listColor);
        dataset.setColors(getResources().getColor(R.color.sky_blue), getResources().getColor(R.color.blue));


        dataset.setDrawIcons(true);
        dataset.setSliceSpace(1f);
        dataset.setIconsOffset(new MPPointF(0, 40));
        dataset.setSelectionShift(2f);


        PieData data = new PieData(dataset);

        data.setValueFormatter(new ValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setHighlightEnabled(true);


        pieChart.setData(data);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(true);
    }

    private int getCorrect() {
        int n = 0;

        for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest question : aspect.getQuestions()) {
            boolean result = true;

            if (question.getCodeQuestion().equals("OPC_SIM")) {
                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative : question.getAlternatives()) {
                    if (alternative.isSelected()) {
                        result = result && "RPTA_COR".equals(alternative.getCodeAlternative());
                    }
                }
            } else {
                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative : question.getAlternatives()) {
                    if (alternative.isSelected()) {
                        result = result && "RPTA_COR".equals(alternative.getCodeAlternative());
                    } else {
                        if ("RPTA_COR".equals(alternative.getCodeAlternative())) {
                            result = false;
                        }
                    }
                }
            }

            if (result)
                n++;

        }
        return n;
    }

    @OnClick(R.id.tvNext)
    public void onNextClicked() {
        startActivity(new Intent(ScoreActivity.this, HomeActivity.class));
    }

    class ValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return String.valueOf((int) value);
        }
    }

    class GestureChart implements OnChartValueSelectedListener {

        @Override
        public void onValueSelected(Entry e, Highlight h) {

            sectionAdapter = new SectionedRecyclerViewAdapter();
            filterQuestion.clear();

            if ("Malas".equals(((PieEntry) e).getLabel())) {

                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest question : aspect.getQuestions()) {

                    EvaluationActiveRequest.EvaluationRequest.QuestionRequest newQuestion = new EvaluationActiveRequest.EvaluationRequest.QuestionRequest(0, 0, question.getQuestion(), question.getCodeQuestion(), new ArrayList<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest>());
                    List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> newAlternative = new ArrayList<>();

                    boolean shouldInsert = false;

                    for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative : question.getAlternatives()) {

                        if (question.getCodeQuestion().equals("OPC_SIM")) {
                            if (alternative.isSelected() && !"RPTA_COR".equals(alternative.getCodeAlternative())) {
                                EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative2 = new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(0, alternative.getDescription(), alternative.getCodeAlternative());
                                alternative2.setSelected(alternative.isSelected());
                                newAlternative.add(alternative2);
                                shouldInsert = true;
                            }
                        }else{
                            if (alternative.isSelected() || "RPTA_COR".equals(alternative.getCodeAlternative())) {
                                EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative2 = new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(0, alternative.getDescription(), alternative.getCodeAlternative());
                                alternative2.setSelected(alternative.isSelected());
                                newAlternative.add(alternative2);
                                shouldInsert = true;
                            }
                        }
                    }

                    if (shouldInsert) {
                        newQuestion.setAlternatives(newAlternative);
                        filterQuestion.add(newQuestion);
                    }
                }
            }

            if ("Buenas".equals(((PieEntry) e).getLabel())) {

                for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest question : aspect.getQuestions()) {

                    EvaluationActiveRequest.EvaluationRequest.QuestionRequest newQuestion = new EvaluationActiveRequest.EvaluationRequest.QuestionRequest(0, 0, question.getQuestion(), question.getCodeQuestion(), new ArrayList<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest>());
                    List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> newAlternative = new ArrayList<>();

                    boolean shoulInsert = false;
                    for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative : question.getAlternatives()) {

                        if ((alternative.isSelected() && "RPTA_COR".equals(alternative.getCodeAlternative()))) {
                            EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest alternative2 = new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(0, alternative.getDescription(), alternative.getCodeAlternative());
                            alternative2.setSelected(alternative.isSelected());
                            newAlternative.add(alternative2);
                            shoulInsert = true;
                        }

                        if (!alternative.isSelected() && "RPTA_COR".equals(alternative.getCodeAlternative())) {
                            shoulInsert = false;
                        }
                    }
                    if (shoulInsert) {
                        filterQuestion.add(newQuestion);
                        newQuestion.setAlternatives(newAlternative);
                    }

                }
            }

            for (EvaluationActiveRequest.EvaluationRequest.QuestionRequest question2 : filterQuestion) {
                sectionAdapter.addSection(new QuestionSection(question2));
            }

            sectionAdapter.notifyDataSetChanged();

            rvResponse.setLayoutManager(new LinearLayoutManager(mContext));
            rvResponse.setAdapter(sectionAdapter);
        }

        @Override
        public void onNothingSelected() {

        }
    }

    class QuestionSection extends StatelessSection {

        EvaluationActiveRequest.EvaluationRequest.QuestionRequest question;

        public QuestionSection(EvaluationActiveRequest.EvaluationRequest.QuestionRequest question) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.item_evaluation_result_item)
                    .headerResourceId(R.layout.item_evaluation_result_header)
                    .build());

            this.question = question;
        }

        @Override
        public int getContentItemsTotal() {
            return question.getAlternatives().size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest item = question.getAlternatives().get(position);
            boolean isCorrect = (item.isSelected() && "RPTA_COR".equals(item.getCodeAlternative())) || (!item.isSelected() && "RPTA_COR".equals(item.getCodeAlternative()));
            itemHolder.bind(isCorrect, item.getDescription());

        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.bind(question.getQuestion());
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.llContenedor)
            LinearLayout llContenedor;

            @BindView(R.id.tvTitle)
            TextViewCustom tvTitle;

            @BindView(R.id.imageView)
            ImageView imageView;


            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(boolean isCorrect, String alternative) {
                tvTitle.setText(alternative);

                if (isCorrect) {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.correct));
                } else {
                    tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.incorrect));
                }
            }


        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tvTitle)
            TextViewBoldCustom tvTitle;

            @BindView(R.id.llContenedor)
            LinearLayout llContenedor;

            public HeaderViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(String title) {
                tvTitle.setText(title);

                //flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_pair));
            }
        }
    }
}
