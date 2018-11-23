package com.qubicgo.android.appformacion.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.custom.TextViewMediumCustom;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ScoreFragment extends Fragment {


    Unbinder unbinder;

    @BindView(R.id.tvTitulo)
    TextViewBoldCustom tvTitulo;

    @BindView(R.id.tvCurso)
    TextViewMediumCustom tvCurso;

    @BindView(R.id.tvPersona)
    TextViewMediumCustom tvPersona;

    @BindView(R.id.tvScore)
    TextViewBoldCustom tvScore;

    @BindView(R.id.tvRespuestas)
    TextViewCustom tvRespuestas;

    @BindView(R.id.pieChart)
    PieChart pieChart;

    public ScoreFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_score, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        double score = bundle.getDouble("score");
        int cantidad = bundle.getInt("cantidad");
        int corrects = bundle.getInt("corrects");

        tvScore.setText(String.valueOf(score));
        tvRespuestas.setText(corrects + "/" + cantidad + " respuestas\ncorrectas");
/*
        tvCurso.setText(ActiveEvaluationsFragment.nombreCurso);
        if (ActiveEvaluationsFragment.expositor==null)
            tvPersona.setVisibility(View.GONE);
        else
            tvPersona.setText(ActiveEvaluationsFragment.expositor);
*/
        loadChart(cantidad - corrects, corrects);

        return view;
    }

    private void loadChart(int incorrects, int corrects) {
        pieChart.setDescription(null);
        pieChart.getLegend().setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(incorrects, ""));
        entries.add(new PieEntry(corrects, ""));
        PieDataSet dataset = new PieDataSet(entries, "");

        List<Integer> listColor = new ArrayList<>();
        listColor.add(getResources().getColor(R.color.blue));
        listColor.add(getResources().getColor(R.color.sky_blue));
        dataset.setValueTextColors(listColor);
        dataset.setColors(getResources().getColor(R.color.blue), getResources().getColor(R.color.sky_blue));

        PieData data = new PieData(dataset);
        pieChart.setData(data);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnFinish)
    public void onBtnFinishClicked() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
