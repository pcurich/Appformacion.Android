package com.qubicgo.android.appformacion.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.activities.EvaluationActivity;
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.activities.MaterialDetailActivity;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.services.def.IServiceEvaluation;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryEvaluation;
import com.qubicgo.android.appformacion.data.request.EvaluationActiveListRequest;
import com.qubicgo.android.appformacion.data.request.EvaluationActiveRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ActiveEvaluationFragment extends Fragment {
    @BindView(R.id.rvEvaluationActive)
    RecyclerView rvEvaluationActive;

    Unbinder unbinder;

    private Context mContext;
    private ProgressDialog progressDialog;
    public static List<EvaluationActiveRequest> list;

    public ActiveEvaluationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_evaluation, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        list = new ArrayList<>();
        mContext = getActivity();

        loadActiveEvaluations();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_home,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                loadActiveEvaluations();
                break;
        }
        return true;
    }

    private  void loadActiveEvaluations(){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva",Context.MODE_PRIVATE);
        String userName = preferences.getString("username", "");

        final IServiceEvaluation evaluationService = ServiceFactoryEvaluation.getListEvaluations(mContext).create(IServiceEvaluation.class);
        Call<EvaluationActiveListRequest> response = evaluationService.obtenerEvaluaciones(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                userName);

        response.enqueue(new Callback<EvaluationActiveListRequest>() {
            @Override
            public void onResponse(Call<EvaluationActiveListRequest> call, Response<EvaluationActiveListRequest> response) {

                EvaluationActiveListRequest result = response.body();

                if("OK".equals( result.getStatus()) && result.getBody()!=null){
                    list.clear();
                    list.addAll(result.getBody());

                    SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
                    Integer position = 1;
                    for (EvaluationActiveRequest element : list){
                        sectionAdapter.addSection(new ActiveEvaluationFragment.EvaluationSection(element.getName(), element.getEvaluations() ,mContext,position,element.getType()));
                        position++;
                    }

                    rvEvaluationActive.setLayoutManager(new LinearLayoutManager(mContext));
                    rvEvaluationActive.setAdapter(sectionAdapter);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EvaluationActiveListRequest> call, Throwable t) {
                progressDialog.dismiss();
                if (t.toString().contains("BEGIN_OBJECT")){
                    HomeActivity.restartSession(mContext);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class EvaluationSection extends StatelessSection {

        String title;
        String type;
        List<EvaluationActiveRequest.EvaluationRequest> list;
        Integer position;
        Context mContext;

        public EvaluationSection(String title,
                           List<EvaluationActiveRequest.EvaluationRequest> list,
                           Context context,
                           Integer position,
                           String type)
        {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.item_active_section_item)
                    .headerResourceId(R.layout.item_active_section_header)
                    .build());

            this.title = title;
            this.list = list;
            this.mContext = context;
            this.position = position;
            this.type = type;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ActiveEvaluationFragment.ItemViewHolder(view,mContext);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ActiveEvaluationFragment.ItemViewHolder itemHolder = (ActiveEvaluationFragment.ItemViewHolder) holder;

            EvaluationActiveRequest.EvaluationRequest  item  = list.get(position);
            itemHolder.bind(item);
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new ActiveEvaluationFragment.HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            ActiveEvaluationFragment.HeaderViewHolder headerHolder = (ActiveEvaluationFragment.HeaderViewHolder) holder;
            headerHolder.bind(position,type,title,mContext);
        }
    }

    class ItemViewHolder  extends RecyclerView.ViewHolder {

        @BindView(R.id.llContenedor)
        LinearLayout llContenedor;

        @BindView(R.id.tvTitle)
        TextViewCustom tvTitle;

        Context mContext;

        public ItemViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            ButterKnife.bind(this, itemView);
        }

        public void bind(final EvaluationActiveRequest.EvaluationRequest  aspect ){

            tvTitle.setText(aspect.getName());
            llContenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Intent intent = new Intent(mContext, EvaluationActivity.class);
                    intent.putExtra("active_evaluation_fragment_aspect",  aspect);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    class HeaderViewHolder  extends RecyclerView.ViewHolder{

        @BindView(R.id.tvTitle)
        TextViewBoldCustom tvTitle;

        @BindView(R.id.tvType)
        TextViewCustom tvType;

        @BindView(R.id.flBackground)
        FrameLayout flBackground;


        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position, String type, String title, Context mContext) {
            tvTitle.setText(title);
            tvType.setText("CURS".equals(type) ? "C":"P");

            if ((position + 1) % 2 == 0) {
                flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_pair));
            } else {
                flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_no_pair));
            }
        }
    }
}
